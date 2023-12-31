package com.example.weatheapp.map.view

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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatheapp.MySharedPreferences
import com.example.weatheapp.R
import com.example.weatheapp.database.ConcreteLocalSource
import com.example.weatheapp.database.FavWeatherEntity
import com.example.weatheapp.databinding.FragmentMapsBinding
import com.example.weatheapp.favourite.viewmodel.FavouriteViewModel
import com.example.weatheapp.favourite.viewmodel.FavouriteViewModelFactory
import com.example.weatheapp.main.MainActivity
import com.example.weatheapp.map.viewmodel.MapViewModel
import com.example.weatheapp.map.viewmodel.MapViewModelFactory
import com.example.weatheapp.models.Repository
import com.example.weatheapp.network.WeatherClient
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
    lateinit var destination:String

    lateinit var favouriteViewModel: FavouriteViewModel
    lateinit var faViewModelFactory: FavouriteViewModelFactory

    private val callback = OnMapReadyCallback { map ->
        googleMap = map

        //handle intial map
        if (destination == Constants.HOME.toString()) {
            googleMap.setOnMapClickListener { latLng ->
                val geocoder =
                    Geocoder(requireContext(), Locale.getDefault())
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
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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
                    Geocoder(requireContext(), Locale.getDefault())
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
                                favouriteViewModel.insertFavWeatherIntoRoom(FavWeatherEntity(lat, lng, city))
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
        destination = mySharedPreferences.getMapDestinationPrefrence()!!
        faViewModelFactory = FavouriteViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(),
                ConcreteLocalSource(requireContext())
            )
        )
        favouriteViewModel =
            ViewModelProvider(this, faViewModelFactory).get(FavouriteViewModel::class.java)

         val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (MySharedPreferences.getInstance(requireContext()).getMapDestinationPrefrence() == Constants.FAVOURITE.toString()) {
            val navView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavBar)
            navView.visibility = View.VISIBLE
        }
    }
}

