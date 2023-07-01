package com.example.weatheapp.settings.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.weatheapp.MySharedPreferences
import com.example.weatheapp.databinding.FragmentSettingBinding
import com.example.weatheapp.settings.viewmodel.SettingViewModel
import com.example.weatheapp.settings.viewmodel.SettingViewModelFactory
import com.example.weatheapp.utilities.Constants

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
                    settingViewModel.setLanguage(Constants.en.toString(), "en")
                }
                binding.settingLanguageArabicRadioButton.id -> {
                    settingViewModel.setLanguage(Constants.ar.toString(), "ar")
                }
            }
        }
        binding.settingLocationRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.settingLocationGpsRadioButton.id -> {
                    settingViewModel.setLocationMethod(Constants.GPS.toString())
                   }
                binding.settingLocationMapRadioButton.id -> {
                    settingViewModel.setLocationMethod(Constants.MAP.toString())
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