package com.example.weatheapp.alert.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
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
import com.example.weatheapp.MySharedPreferences
import com.example.weatheapp.R
import com.example.weatheapp.alert.viewmodel.AlertViewModel
import com.example.weatheapp.alert.viewmodel.AlertViewModelFactory
import com.example.weatheapp.database.*
import com.example.weatheapp.databinding.AlertDialogBinding
import com.example.weatheapp.databinding.FragmentAlertBinding
import com.example.weatheapp.model.Repository
import com.example.weatheapp.network.ApiClient
import com.example.weatheapp.utilities.getDateInMillis
import com.example.weatheapp.utilities.getTimeInMillis
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alertiewModelFactory = AlertViewModelFactory(
            Repository.getInstance(
                ApiClient.getInstance(),
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

        lifecycleScope.launch {
            alertViewModel.getAlertWeatherFromRoom()
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

                }
            }
        }

        binding.alertFAB.setOnClickListener {
            val isPermissionGranted = requestOverlayPermission()
            if(isPermissionGranted){
            displayAlertDialog()
            }else{
                Toast.makeText(requireContext(),"permission must be granted to add alert",Toast.LENGTH_SHORT)
            }
        }
    }

    private val deleteLambda = { alert: AlertWeatherEntity ->
        alertViewModel.deleteALertWeatherFromRoom(alert)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun displayAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val alertDialog = AlertDialogBinding.inflate(layoutInflater)
        builder.setView(alertDialog.root)
        val dialog = builder.create()
        dialog.show()
        val fromDate = alertDialog.alertDialogFromDateTV
        val toDate = alertDialog.alertDialogToDateTV
        val fromTime = alertDialog.alertDialogFromTimeTV
        val toTime = alertDialog.alertDialogToTimeTV
        alertDialog.alertDialogFromTV.setOnClickListener {
            showDatePicker("start", fromDate, fromTime)
        }
        alertDialog.alertDialogToTV.setOnClickListener {
            showDatePicker("end", toDate, toTime)
        }
        alertDialog.alertDialogSaveBtn.setOnClickListener {
            val alertEntity=AlertWeatherEntity(startDate = startDate, startTime = startTime, endDate = endDate, endTime = endTime)
            alertViewModel.insertALertWeatherFromRoom(alertEntity)
            dialog.dismiss()
            val fragment = AlertFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host, fragment)
            transaction.commit()

        }

    }

    fun showDatePicker(selectedDateType: String, date: TextView, time: TextView) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // Create a calendar instance and set the selected date
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // You can then use the selectedDate variable to get the year, month, and day
                val selectedDateString = SimpleDateFormat(
                    "EEE d MMM yyyy",
                    Locale(mySharedPreferences.getLanguagePreference()!!)
                ).format(selectedDate.time)

                // Do something with the selected date
                Log.i("Calendar", "showDatePicker: $selectedDateString")

                // Save the selected date in the appropriate variables based on the given parameter
                if (selectedDateType == "start") {
                    startDateString = selectedDateString
                    startDate = getDateInMillis(dayOfMonth,month,year)

                    date.text = startDateString
                } else if (selectedDateType == "end") {
                    endDateString = selectedDateString
                    endDate = getDateInMillis(dayOfMonth,month,year)

                    date.text = endDateString

                }

                // Call the showTimePicker function after the user selects a date
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

        // Show the date picker dialog
        datePickerDialog.show()
    }

    fun showTimePicker(selectedTimeType: String, time: TextView) {
        val calendar = Calendar.getInstance()

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                // Create a calendar instance and set the selected time
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)

                val selectedTimeString = SimpleDateFormat(
                    "HH:mm",
                    Locale(mySharedPreferences.getLanguagePreference()!!)
                ).format(selectedTime.time)
                val amPm = if (hourOfDay > 12) "${context?.getString(R.string.pm)}" else "${
                    context?.getString(R.string.am)
                }"
                if (selectedTimeType == "start") {
                    startTime = getTimeInMillis(hourOfDay,minute,amPm)
                    startTimeString = "$selectedTimeString $amPm"
                    time.text = startTimeString
                } else if (selectedTimeType == "end") {
                    endTime = getTimeInMillis(hourOfDay,minute,amPm)
                    endTimeString = "$selectedTimeString $amPm"
                    time.text = endTimeString
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        timePickerDialog.show()
    }
    private val REQUEST_OVERLAY_PERMISSION = 1000
    val packageName= "com.example.weatheapp"
    @RequiresApi(Build.VERSION_CODES.M)
    fun requestOverlayPermission(): Boolean {
        if (!Settings.canDrawOverlays(requireContext())) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Permission Required")
            builder.setMessage("This app requires the 'Display over other apps' permission to function properly.")
            builder.setPositiveButton("Grant Permission") { _, _ ->
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
                startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            builder.setCancelable(false)
            builder.show()
            return false
        } else {
            // Permission granted, do your work here
            return true
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1000) {
            if (Settings.canDrawOverlays(requireContext())) {
                // Permission granted, do your work here
            } else {
                // Permission not granted
            }
        }
}}


/*    @RequiresApi(Build.VERSION_CODES.M)
    fun askForDrawOverlaysPermission(callback: () -> Unit) {
        if (!Settings.canDrawOverlays(requireView().context)) {
            AlertDialog.Builder(requireView().context)
                .setTitle("Permission Required")
                .setMessage("This feature requires permission to draw over other apps. Please grant the permission to enable this feature.")
                .setPositiveButton(R.string.ok) { _, _ ->
                    (!Settings.canDrawOverlays(this)) {
                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
                        startActivityForResult(intent, 1000)
                }
                .show()
        } else {
            callback() // Call the callback if the permission is already granted
        }
    }
}*/

