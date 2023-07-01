package com.example.weatheapp.settings.view

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModelProvider
import com.example.weatheapp.MySharedPreferences
import com.example.weatheapp.databinding.FragmentSettingBinding
import com.example.weatheapp.home.viewmodel.HomeViewModel
import com.example.weatheapp.home.viewmodel.HomeViewModelFactory
import com.example.weatheapp.model.Repository
import com.example.weatheapp.network.ApiClient
import com.example.weatheapp.settings.viewmodel.SettingViewModel
import com.example.weatheapp.settings.viewmodel.SettingViewModelFactory
import com.example.weatheapp.utilities.Constants
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
        settingViewModel =
            ViewModelProvider(this, settingViewModelFactory).get(SettingViewModel::class.java)

        when (settingViewModel.getLanguage()) {
            Constants.ENGLISH.toString() -> binding.settingLanguageEnglishRadioButton.isChecked =
                true
            Constants.ARABIC.toString() -> binding.settingLanguageArabicRadioButton.isChecked = true
        }
        when (settingViewModel.getLocationMethod()) {
            Constants.GPS.toString() -> binding.settingLocationGpsRadioButton.isChecked = true
            Constants.MAP.toString() -> binding.settingLocationMapRadioButton.isChecked = true
        }
        when (settingViewModel.getNotificationStatus()) {
            Constants.ENABLED.toString() -> binding.settingNotificationEnabledRadioButton.isChecked =
                true
            Constants.DISABLED.toString() -> binding.settingNotificationDisableRadioButton.isChecked =
                true
        }


        // Save the selected language preference when a radio button is clicked
        binding.settingLanguageRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.settingLanguageEnglishRadioButton.id -> {
                    settingViewModel.setLanguage(Constants.ENGLISH.toString(), "en")
                }
                binding.settingLanguageArabicRadioButton.id -> {
                    settingViewModel.setLanguage(Constants.ARABIC.toString(), "ar")
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
    }

}