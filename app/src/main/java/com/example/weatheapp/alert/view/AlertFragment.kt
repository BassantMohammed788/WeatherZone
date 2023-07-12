package com.example.weatheapp.alert.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.weatheapp.MySharedPreferences
import com.example.weatheapp.R
import com.example.weatheapp.alert.AlertWorker
import com.example.weatheapp.alert.viewmodel.AlertViewModel
import com.example.weatheapp.alert.viewmodel.AlertViewModelFactory
import com.example.weatheapp.database.*
import com.example.weatheapp.databinding.AlertDialogBinding
import com.example.weatheapp.databinding.FragmentAlertBinding
import com.example.weatheapp.models.Repository
import com.example.weatheapp.network.WeatherClient
import com.example.weatheapp.utilities.*
import com.google.gson.Gson
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class AlertFragment : Fragment() {
    lateinit var binding: FragmentAlertBinding
    lateinit var mySharedPreferences: MySharedPreferences
    lateinit var alertViewModel: AlertViewModel
    lateinit var alertiewModelFactory: AlertViewModelFactory
    lateinit var alertWeatherAdapter: AlertWeatherAdapter
    lateinit var recyclerView: RecyclerView
    private var startDateString: String = ""
    private var endDateString: String = ""
    private var startTimeString: String = ""
    private var endTimeString: String = ""
    private var startDate: Long = 0
    private var endDate:Long = 0
    private var startTime:Long = 0
    private var endTime:Long = 0
    private var alarmOrNotification : String = ""
    private var description = ""
    private var isWorkRequestEnqueued = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        mySharedPreferences = MySharedPreferences.getInstance(requireContext())
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alertiewModelFactory = AlertViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(),
                ConcreteLocalSource(requireContext())
            )
        )
        alertViewModel =
            ViewModelProvider(this, alertiewModelFactory).get(AlertViewModel::class.java)

        alertWeatherAdapter = AlertWeatherAdapter(mySharedPreferences,deleteLambda)
        recyclerView = binding.alertRecyclerView
        recyclerView.apply {
            adapter = alertWeatherAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        alertViewModel.getAlertWeatherFromRoom()
        lifecycleScope.launch {
            alertViewModel.alert.collectLatest { result ->
                when (result) {
                    is LocalAlertState.Loading -> {
                        binding.NoAlertIcon.visibility = View.VISIBLE
                        binding.noAerttextView.visibility = View.VISIBLE
                        binding.alertRecyclerView.visibility = View.GONE
                    }
                    is LocalAlertState.Success -> {
                        if (result.alertWeather.isEmpty()) {
                            binding.NoAlertIcon.visibility = View.VISIBLE
                            binding.noAerttextView.visibility = View.VISIBLE
                            binding.alertRecyclerView.visibility = View.GONE
                        } else {
                            binding.NoAlertIcon.visibility = View.GONE
                            binding.noAerttextView.visibility = View.GONE
                            binding.alertRecyclerView.visibility = View.VISIBLE
                            alertWeatherAdapter.submitList(result.alertWeather)
                        }
                    }
                    is LocalAlertState.Failure -> {
                        Log.i("TAG", "onCreate: failed")
                        Toast.makeText(requireContext(), "failed to load data", Toast.LENGTH_LONG)
                            .show()
                    }

                    else -> {}
                }
            }
        }

        binding.alertFAB.setOnClickListener {
            val isPermissionGranted = requestOverlayPermission(this)
            if(isPermissionGranted){
            displayAlertDialog()
            }else{
                Toast.makeText(requireContext(),R.string.permissionrequiredtoaddalert,Toast.LENGTH_SHORT)
            }
        }
    }

    private val deleteLambda = { alert: AlertWeatherEntity ->
        val worker = WorkManager.getInstance(requireContext())
        worker.cancelAllWorkByTag(alert.id)
        alertViewModel.deleteALertWeatherFromRoom(alert)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun displayAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val alertDialog = AlertDialogBinding.inflate(layoutInflater)
        builder.setView(alertDialog.root)
        val dialog = builder.create()
        dialog.show()
        val fromTv =   alertDialog.alertDialogFromTV
        val toTv = alertDialog.alertDialogToTV
        val fromDate = alertDialog.alertDialogFromDateTV
        val toDate = alertDialog.alertDialogToDateTV
        val fromTime = alertDialog.alertDialogFromTimeTV
        val toTime = alertDialog.alertDialogToTimeTV
        val notificationRadio = alertDialog.alertDialogRadioGroup
        fromTv.setOnClickListener {
            showDatePicker("start", fromDate, fromTime)
        }
        toTv.setOnClickListener {
            showDatePicker("end", toDate, toTime)
        }
        notificationRadio.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                alertDialog.alertDialogAlarmRadioButton.id -> {
                    alarmOrNotification=Constants.ALARM.toString()
                }
                alertDialog.alertDialogNotificationRadioButton.id -> {
                    alarmOrNotification=Constants.NOTIFICATION.toString()
                }
            }
        }
        alertDialog.alertDialogSaveBtn.setOnClickListener {
            if (startDateString.isEmpty() ||  startTimeString.isEmpty() ) {
                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                    .setMessage(R.string.alertStartRequired)
                    .setPositiveButton(R.string.ok, null)
                alertDialogBuilder.show()
            }else if(endDateString.isEmpty()|| endTimeString.isEmpty()){
                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                    .setMessage(R.string.alertEndRequired)
                    .setPositiveButton(R.string.ok, null)
                alertDialogBuilder.show()

            } else if (alertDialog.alertDialogRadioGroup.checkedRadioButtonId == -1) {
                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                    .setMessage(R.string.alertTypeNotSelected)
                    .setPositiveButton(R.string.ok, null)
                alertDialogBuilder.show()
            } else {
                val alertEntity = AlertWeatherEntity(startDate = startDate, startTime = startTime, endDate = endDate, endTime = endTime, alertType = alarmOrNotification)
                alertViewModel.insertALertWeatherFromRoom(alertEntity)

                val gson = Gson()
                val alertJsonString = gson.toJson(alertEntity)
                Log.i("TAG", "jsonString: $alertJsonString")

               /* lifecycleScope.launch {
                        alertViewModel.getWeatherFromRoom()
                        alertViewModel.homeWeather.collectLatest { result ->
                            when (result) {
                                is RoomState.Success -> {
                                    Log.i("TAG", "displayAlertDialog: ${alertEntity.id}")
                                    var data = result.weather.alert
                                    if (data != null) {
                                        if (data.isEmpty()) {
                                            description = "Weather is fine"
                                        } else {
                                            description = data[0].tags[0]
                                        }
                                    }else{
                                        description = "Weather is fine"
                                    }
                                    val currentDateTimeInMillis = System.currentTimeMillis()
                                    val delay = startTime-currentDateTimeInMillis

                                    Log.i("TAG", "start: ${formatDate(startTime)} ")

                                    Log.i("TAG", "end: ${formatDate(endTime)} ")
                                    Log.i("TAG", "current: ${formatDate(currentDateTimeInMillis)} ")

                                    if (!isWorkRequestEnqueued) {
                                        val inputData = Data.Builder()
                                            .putString(
                                                Constants.ALERT_JSON_STRING.toString(),
                                                alertJsonString
                                            ).build()

                                        val myRequest: WorkRequest =
                                            OneTimeWorkRequestBuilder<AlertWorker>()
                                                .setInputData(inputData)
                                                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                                                .addTag(alertEntity.id)
                                                .build()


                                        Log.i("TAG", "displayAlertDialog: request")
                                        WorkManager.getInstance(requireContext())
                                            .enqueue(myRequest)
                                        Log.i("TAG", "displayAlertDialog: enqueue")
                                        isWorkRequestEnqueued = true
                                    }
                                    dialog.dismiss()
                                    val fragment = AlertFragment()
                                    val transaction = parentFragmentManager.beginTransaction()
                                    transaction.replace(R.id.nav_host, fragment)
                                    transaction.commit()
                                }
                                else -> {}
                            }
                        }
                }*/
                val currentDateTimeInMillis = System.currentTimeMillis()
                val delay = startTime-currentDateTimeInMillis

                Log.i("TAG", "start: ${formatDate(startTime)} ")

                Log.i("TAG", "end: ${formatDate(endTime)} ")
                Log.i("TAG", "current: ${formatDate(currentDateTimeInMillis)} ")

                if (!isWorkRequestEnqueued) {
                    val inputData = Data.Builder()
                        .putString(
                            Constants.ALERT_JSON_STRING.toString(),
                            alertJsonString
                        ).build()

                    val myRequest: WorkRequest =
                        OneTimeWorkRequestBuilder<AlertWorker>()
                            .setInputData(inputData)
                            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                            .addTag(alertEntity.id)
                            .build()


                    Log.i("TAG", "displayAlertDialog: request")
                    WorkManager.getInstance(requireContext())
                        .enqueue(myRequest)
                    Log.i("TAG", "displayAlertDialog: enqueue")
                    isWorkRequestEnqueued = true
                }
                dialog.dismiss()
                val fragment = AlertFragment()
                val transaction = parentFragmentManager.beginTransaction()
                transaction.replace(R.id.nav_host, fragment)
                transaction.commit()
            }
        }
    }

    fun showDatePicker(selectedDateType: String, date: TextView, time: TextView) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->

                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val selectedDateString = SimpleDateFormat("EEE d MMM yyyy", Locale(mySharedPreferences.getLanguagePreference()!!)).format(selectedDate.time)

                if (selectedDateType == "start") {
                    startDateString = selectedDateString
                    startDate = getDateInMillis(dayOfMonth,month,year)

                    date.text = startDateString
                } else if (selectedDateType == "end") {
                    endDateString = selectedDateString
                    endDate = getDateInMillis(dayOfMonth,month,year)
                    date.text = endDateString
                }
                showTimePicker(selectedDateType, time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Disable last days, months, and years
        val minDate = System.currentTimeMillis()
        val maxDate = calendar.apply {
            add(Calendar.YEAR, 2)
        }.timeInMillis
        datePickerDialog.datePicker.minDate = minDate
        datePickerDialog.datePicker.maxDate = maxDate

        datePickerDialog.show()
    }

    fun showTimePicker(selectedTimeType: String, time: TextView) {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)

                val selectedTimeString = SimpleDateFormat("h:mm a", Locale(mySharedPreferences.getLanguagePreference()!!)).format(selectedTime.time)

                if (selectedTimeType == "start") {
                    startTime = getTimeInMillis(hourOfDay,minute)
                    startTimeString = selectedTimeString
                    time.text = startTimeString
                } else if (selectedTimeType == "end") {
                    endTime = getTimeInMillis(hourOfDay,minute)
                    endTimeString = selectedTimeString
                    time.text = endTimeString
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
    }
}