package com.example.weatheapp.alert.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.weatheapp.MySharedPreferences
import com.example.weatheapp.R
import com.example.weatheapp.alert.viewmodel.AlertViewModel
import com.example.weatheapp.alert.viewmodel.AlertViewModelFactory
import com.example.weatheapp.database.ConcreteLocalSource
import com.example.weatheapp.databinding.AlertDialogBinding
import com.example.weatheapp.databinding.FragmentAlertBinding
import com.example.weatheapp.model.Repository
import com.example.weatheapp.network.ApiClient
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
    private var startAmPmString: String = ""
    private var endAmPmString: String = ""
    private var startYear: Int = 0
    private var startMonth: Int = 0
    private var startDay: Int = 0
    private var endYear: Int = 0
    private var endMonth: Int = 0
    private var endDay: Int = 0
    private var startHour = 0
    private var endHour = 0
    private var startMinute = 0
    private var endMinute = 0


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



        binding.alertFAB.setOnClickListener {
            displayAlertDialog()
        }
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

            //askForDrawOverlaysPermission({})
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
                    startDay = dayOfMonth
                    startMonth = month
                    startYear = year
                    date.text = startDateString
                } else if (selectedDateType == "end") {
                    endDateString = selectedDateString
                    endDay = dayOfMonth
                    endMonth = month
                    endYear = year
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
                    startHour = hourOfDay
                    startMinute = minute
                    startAmPmString = amPm
                    startTimeString = "$selectedTimeString $amPm"
                    time.text = startTimeString
                } else if (selectedTimeType == "end") {
                    endHour = hourOfDay
                    endMinute = minute
                    endAmPmString=amPm
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

    fun getUnixTimestamp(hour: Int, amPm: String, minute: Int, day: Int, month: Int, year: Int): Long {
        val calendar = Calendar.getInstance()

        // Set the year, month, and day
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1) // Note: month is 0-based (0 = January)
        calendar.set(Calendar.DAY_OF_MONTH, day)

        // Set the hour and minute
        val hourOfDay = if (amPm.equals("PM", ignoreCase = true)) {
            if (hour != 12) hour + 12 else 12
        } else {
            if (hour == 12) 0 else hour
        }
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        // Set the seconds and milliseconds to 0
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // Convert the Calendar instance to a Unix timestamp
        return calendar.timeInMillis / 1000L
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun askForDrawOverlaysPermission(callback: () -> Unit) {
        if (!Settings.canDrawOverlays(requireView().context)) {
            AlertDialog.Builder(requireView().context)
                .setTitle("Permission Required")
                .setMessage("This feature requires permission to draw over other apps. Please grant the permission to enable this feature.")
                .setPositiveButton(R.string.ok) { _, _ ->
                    val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
                    intent.setClassName(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.permissions.PermissionsEditorActivity"
                    )
                    intent.putExtra("extra_pkgname", requireView().context.packageName)
                   // runtimePermissionResultLauncher.launch(intent)
                }
                .show()
        } else {
            callback() // Call the callback if the permission is already granted
        }
    }
}
