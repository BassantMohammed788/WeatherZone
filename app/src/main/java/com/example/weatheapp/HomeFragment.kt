package com.example.weatheapp

import MyLocation
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class HomeFragment : Fragment() {

    val location = MyLocation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (location.lat != null && location.lng != null) {
            Log.d("LocationFragment", "Latitude: ${location.lat}, Longitude: ${location.lng}")
        } else {
            Log.e("LocationFragment", "MyLocation not set")
        }

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

}