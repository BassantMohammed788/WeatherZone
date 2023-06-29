package com.example.weatheapp

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
import com.example.weatheapp.mainactivity.MainActivity

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsFragment : Fragment() {

    private lateinit var googleMap: GoogleMap
    private val location = MyLocation // Get the singleton object

    private val callback = OnMapReadyCallback { map ->
        googleMap = map

        googleMap.setOnMapClickListener { latLng ->
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (addresses!!.isNotEmpty()) {
                val address = addresses[0]
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Are you sure you want to choose this location?\n${address.getAddressLine(0)}")
                builder.setPositiveButton("OK") { dialog, it ->
                        location.lat = latLng.latitude
                        location.lng = latLng.longitude
                        Toast.makeText(requireContext(), "MyLocation saved: ${address.locality}", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)

                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                }
                val dialog = builder.create()
                dialog.show()
            } else {
                Toast.makeText(requireContext(), "Could not find location", Toast.LENGTH_SHORT).show()
            }
        }

        val alex = LatLng(30.0197105, 31.2681736)
        googleMap.addMarker(MarkerOptions().position(alex).title("Marker in Alex"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(alex))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}