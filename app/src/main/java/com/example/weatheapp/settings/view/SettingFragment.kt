package com.example.weatheapp.settings.view

import MyLocation
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.weatheapp.MySharedPreferences
import com.example.weatheapp.R
import com.example.weatheapp.databinding.FragmentSettingBinding
import com.example.weatheapp.main.MainActivity
import com.example.weatheapp.settings.viewmodel.SettingViewModel
import com.example.weatheapp.settings.viewmodel.SettingViewModelFactory
import com.example.weatheapp.utilities.Constants
import com.example.weatheapp.utilities.isConnected
import com.google.android.material.snackbar.Snackbar
import getLastLocation
import java.util.*

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private lateinit var settingViewModelFactory: SettingViewModelFactory
    lateinit var settingViewModel: SettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mySharedPreferences = MySharedPreferences.getInstance(requireContext())
        settingViewModelFactory = SettingViewModelFactory(mySharedPreferences)
        settingViewModel = ViewModelProvider(this, settingViewModelFactory).get(SettingViewModel::class.java)

        when (settingViewModel.getLanguage()) {
            Constants.en.toString() -> binding.settingLanguageEnglishRadioButton.isChecked = true
            Constants.ar.toString() -> binding.settingLanguageArabicRadioButton.isChecked = true
        }
        when (settingViewModel.getLocationMethod()) {
            Constants.GPS.toString() -> binding.settingLocationGpsRadioButton.isChecked = true
            Constants.MAP.toString() -> binding.settingLocationMapRadioButton.isChecked = true
        }
        when (settingViewModel.getNotificationStatus()) {
            Constants.ENABLED.toString() -> binding.settingNotificationEnabledRadioButton.isChecked = true
            Constants.DISABLED.toString() -> binding.settingNotificationDisableRadioButton.isChecked = true
        }
        when (settingViewModel.getTempratureUnit()) {
            Constants.imperial.toString() -> binding.settingTempratureFahrenheitRadioButton.isChecked = true
            Constants.metric.toString() -> binding.settingTempratureCelsiusRadioButton.isChecked = true
            Constants.standard.toString() -> binding.settingTempratureKelvinRadioButton.isChecked = true
        }
        when (settingViewModel.getWindSpeedUnit()) {
            Constants.metr.toString() -> binding.settingWindMeterRadioButton.isChecked = true
            Constants.mile.toString() -> binding.settingWindMileRadioButton.isChecked = true
        }



        // Save the selected language preference when a radio button is clicked
        binding.settingLanguageRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.settingLanguageEnglishRadioButton.id -> {
                    settingViewModel.setLanguage(Constants.en.toString())

                    activity?.recreate()

                }
                binding.settingLanguageArabicRadioButton.id -> {
                    settingViewModel.setLanguage(Constants.ar.toString())
                    activity?.recreate()
                }
            }
        }
        binding.settingLocationRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.settingLocationGpsRadioButton.id -> {
                    settingViewModel.setLocationMethod(Constants.GPS.toString())
                   // getLastLocation(requireContext(),{})
                    getLastLocation(requireContext()) { location ->
                    }}
                binding.settingLocationMapRadioButton.id -> {
                    if (isConnected(requireContext())){
                    settingViewModel.setLocationMethod(Constants.MAP.toString())
                    mySharedPreferences.saveMapDestination(Constants.HOME.toString())
                    Navigation.findNavController(view).navigate(R.id.action_menuNavSettingID_to_mapsFragment)
                    }else{
                        Snackbar.make(view, R.string.CheckYourconnection, Snackbar.LENGTH_LONG).show()
                    }
                }

            }
        }
        binding.settingNotificationRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.settingNotificationEnabledRadioButton.id -> {
                    settingViewModel.setLocationMethod(Constants.ENABLED.toString())
                }
                binding.settingNotificationDisableRadioButton.id -> {
                    settingViewModel.setLocationMethod(Constants.DISABLED.toString())
                }
            }

        }
        binding.settingTembratureRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.settingTempratureCelsiusRadioButton.id -> {
                    settingViewModel.setTempratureUnit(Constants.metric.toString())
                    Log.i("temp", "onViewCreated: "+mySharedPreferences.getTempratureUnitPreference())
                    Log.i("temp", "onViewCreated: helloooo")

                }
                binding.settingTempratureKelvinRadioButton.id -> {
                    settingViewModel.setTempratureUnit(Constants.standard.toString())
                    Log.i("temp", "onViewCreated: "+mySharedPreferences.getTempratureUnitPreference())
                }
                binding.settingTempratureFahrenheitRadioButton.id -> {
                    settingViewModel.setTempratureUnit(Constants.imperial.toString())
                    Log.i("temp", "onViewCreated: "+mySharedPreferences.getTempratureUnitPreference())
                }

            }
        }
        binding.settingWindRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.settingWindMeterRadioButton.id -> {
                    settingViewModel.setWindSpeedUnit(Constants.metr.toString())
                    Log.i("temp", "onViewCreated: "+mySharedPreferences.getWindSpeedPreference())

                }
                binding.settingWindMileRadioButton.id -> {
                    settingViewModel.setWindSpeedUnit(Constants.mile.toString())
                }
            } }
    }

}



