package com.example.weatheapp.intialsetup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.weatheapp.MySharedPreferences
import com.example.weatheapp.R
import com.example.weatheapp.databinding.FragmentIntialSetupBinding
import com.example.weatheapp.main.MainActivity
import com.example.weatheapp.map.view.MapsFragment
import com.example.weatheapp.utilities.Constants
import com.example.weatheapp.utilities.isConnected
import com.google.android.material.snackbar.Snackbar
import getLastLocation

class IntialSetupFragment : Fragment() {
    lateinit var binding: FragmentIntialSetupBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentIntialSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mySharedPreferences = MySharedPreferences.getInstance(requireContext())
        binding.intialRadioGroup.clearCheck()
        binding.intialNotificationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mySharedPreferences.saveNotificationStatusPreference(Constants.ENABLED.toString())
            } else {
                mySharedPreferences.saveNotificationStatusPreference(Constants.DISABLED.toString())
            }}
            binding.intialOkBtn.setOnClickListener {
            mySharedPreferences.saveTempratureUnitPreference(Constants.standard.toString())
            mySharedPreferences.saveWindSpeedUnitPreference(Constants.metr.toString())
            // Check which radio button is selected and take action accordingly
            val selectedRadioButtonId = binding.intialRadioGroup.checkedRadioButtonId
            when (selectedRadioButtonId) {
                binding.intialMapRadio.id -> {
                    if (isConnected(requireContext())){
                        mySharedPreferences.saveLocationMethodPreference(Constants.MAP.toString())
                        mySharedPreferences.saveMapDestinationPrefrence(Constants.HOME.toString())
                        val fragment = MapsFragment()
                        val transaction = parentFragmentManager.beginTransaction()
                        transaction.replace(R.id.intialSetupFragment_container, fragment)
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }else{
                        Snackbar.make(view, R.string.CheckYourconnection, Snackbar.LENGTH_LONG).show()
                    }

                }
                binding.intialGpsRadio.id -> {
                    mySharedPreferences.saveLocationMethodPreference(Constants.GPS.toString())
                    binding.gpsProgreesBar.visibility = View.VISIBLE // Show the progress bar
                    getLastLocation(requireContext()) { location ->
                        binding.gpsProgreesBar.visibility = View.GONE // Hide the progress bar
                        if (location != null) {
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {
                            // Location not found
                            Log.d("TAG", "Could not get location")
                        }
                    }
                }
                else -> {
                    Toast.makeText(
                        requireContext(),
                        "Please select a location tool",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }
}

