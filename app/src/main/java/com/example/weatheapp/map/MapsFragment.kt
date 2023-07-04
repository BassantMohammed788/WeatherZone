package com.example.weatheapp.map

import MyLocation
import android.app.AlertDialog
import android.content.Intent
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.weatheapp.MySharedPreferences
import com.example.weatheapp.R
import com.example.weatheapp.database.ConcreteLocalSource
import com.example.weatheapp.database.FavWeatherPojo
import com.example.weatheapp.databinding.FragmentMapsBinding
import com.example.weatheapp.favourite.view.FavouriteFragment
import com.example.weatheapp.mainactivity.MainActivity
import com.example.weatheapp.model.Repository
import com.example.weatheapp.network.ApiClient
import com.example.weatheapp.utilities.Constants

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import java.util.*


class MapsFragment : Fragment() {

    private lateinit var googleMap: GoogleMap
    private val location = MyLocation
    private lateinit var mySharedPreferences: MySharedPreferences
    private lateinit var binding: FragmentMapsBinding
    lateinit var repository: Repository
    lateinit var destination:String

    private val callback = OnMapReadyCallback { map ->
        googleMap = map

        //handle intial map
        if (destination == Constants.HOME.toString()) {
            googleMap.setOnMapClickListener { latLng ->
                val geocoder =
                    Geocoder(requireContext(), Locale(mySharedPreferences.getLanguagePreference()!!))
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (addresses!!.isNotEmpty()) {
                    val address = addresses[0]
                    val city = address.adminArea
                    if (!city.isNullOrEmpty()) {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage(
                            "${context?.getString(R.string.messageHomeMapAlert)} $city"
                        )
                        builder.setPositiveButton("${context?.getString(R.string.yesMapAlert)}") { _, _ ->
                            location.lat = latLng.latitude
                            location.lng = latLng.longitude
                            Toast.makeText(
                                requireContext(),
                                "MyLocation saved: ${address.adminArea}",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                        }
                        builder.setNegativeButton("${context?.getString(R.string.CancelMapAlert)}") { _, _ -> }
                        val dialog = builder.create()
                        dialog.show()
                    } else {
                        Toast.makeText(requireContext(), "${context?.getString(R.string.chooseSpecificPlace)}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "${context?.getString(R.string.errorFavMapAlert)}", Toast.LENGTH_SHORT).show()
                }
            }
        }


        //handle fav map
        else if (destination == Constants.FAVOURITE.toString()) {
            val navView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavBar)
            navView.visibility = View.GONE
            googleMap.setOnMapClickListener { latLng ->
                val geocoder =
                    Geocoder(requireContext(), Locale(mySharedPreferences.getLanguagePreference()!!))
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (addresses!!.isNotEmpty()) {
                    val address = addresses[0]
                    val city = address.adminArea
                    if (!city.isNullOrEmpty()) {
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage("${context?.getString(R.string.messageFavMapAlert)} $city ?")
                        builder.setPositiveButton("${context?.getString(R.string.yesMapAlert)}") { dialog, it ->
                            val lat = latLng.latitude
                            val lng = latLng.longitude
                            lifecycleScope.launch {
                                repository.insertFavWeather(FavWeatherPojo(lat, lng, city))
                            }
                            Toast.makeText(
                                requireContext(),
                                "Location saved: $city",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                        builder.setNegativeButton("${context?.getString(R.string.CancelMapAlert)}") { dialog, which ->
                        }
                        val dialog = builder.create()
                        dialog.show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "${context?.getString(R.string.chooseSpecificPlace)}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "${context?.getString(R.string.errorFavMapAlert)}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        val alex = LatLng(location.lat!!, location.lng!!)
        googleMap.addMarker(MarkerOptions().position(alex).title("Marker"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(alex))
        googleMap.setMinZoomPreference(1f)
        googleMap.setMaxZoomPreference(7f)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mySharedPreferences = MySharedPreferences.getInstance(requireContext())
        repository = Repository.getInstance(ApiClient.getInstance(), ConcreteLocalSource(requireContext()))
        destination = mySharedPreferences.getMapDestination()!!
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (MySharedPreferences.getInstance(requireContext()).getMapDestination() == Constants.FAVOURITE.toString()) {
            val navView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavBar)
            navView.visibility = View.VISIBLE
        }
    }
}

