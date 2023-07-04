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
import com.example.weatheapp.mainactivity.MainActivity
import com.example.weatheapp.map.MapsFragment
import com.example.weatheapp.utilities.Constants
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
        binding.intialOkBtn.setOnClickListener {
            mySharedPreferences.saveTempratureUnitPreference(Constants.standard.toString())
            mySharedPreferences.saveWindSpeedUnitPreference(Constants.metr.toString())
            // Check which radio button is selected and take action accordingly
            val selectedRadioButtonId = binding.intialRadioGroup.checkedRadioButtonId
            when (selectedRadioButtonId) {
                binding.intialMapRadio.id -> {
                    mySharedPreferences.saveLocationMethodPreference(Constants.MAP.toString())
                    mySharedPreferences.saveMapDestination(Constants.HOME.toString())
                    val fragment = MapsFragment()
                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.intialSetupFragment_container, fragment)
                    transaction.addToBackStack(null)
                    transaction.commit()
                }
                binding.intialGpsRadio.id -> {
                    mySharedPreferences.saveLocationMethodPreference(Constants.GPS.toString())
                    binding.gpsProgreesBar.visibility = View.VISIBLE // Show the progress bar
                    getLastLocation(requireContext()) { location ->
                        binding.gpsProgreesBar.visibility = View.GONE // Hide the progress bar
                        if (location != null) {
                            // Location found, do something with it
                            Toast.makeText(
                                requireContext(),
                                "Latitude: ${location.lat}, Longitude: ${location.lng}",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(requireContext(), MainActivity::class.java)
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

