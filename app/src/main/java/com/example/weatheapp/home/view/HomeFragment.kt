package com.example.weatheapp.home.view

import MyLocation
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatheapp.databinding.FragmentHomeBinding
import com.example.weatheapp.home.viewmodel.HomeViewModel
import com.example.weatheapp.home.viewmodel.HomeViewModelFactory
import com.example.weatheapp.model.Repository
import com.example.weatheapp.network.ApiClient
import com.example.weatheapp.network.ApiState
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    val location = MyLocation
   lateinit var homeViewModelFactory : HomeViewModelFactory
   lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

     /*   if (location.lat != null && location.lng != null) {
            Log.d("LocationFragment", "Latitude: ${location.lat}, Longitude: ${location.lng}")
        } else {
            Log.e("LocationFragment", "MyLocation not set")
        }*/



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModelFactory = HomeViewModelFactory(Repository.getInstance(ApiClient.getInstance()))

        homeViewModel = ViewModelProvider(this,homeViewModelFactory).get(HomeViewModel::class.java)


        lifecycleScope.launch {
            homeViewModel.getAllProductOverNetwork(30.0196353,31.268198,"imperial","ar")
            homeViewModel.weather.collectLatest { result ->
                when (result) {
                    is ApiState.Loading -> {/*
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE*/
                        Toast.makeText(requireContext(),"Loaading",Toast.LENGTH_LONG).show()

                    }
                    is ApiState.Failure ->{
                       /* progressBar.visibility = View.GONE*/
                        Log.i("TAG", "onCreate: failed")
                        Toast.makeText(requireContext(),"failed to load data",Toast.LENGTH_LONG).show()
                    }
                    is ApiState.Success->{
                        binding.homeCityTv.text = result.weather.timezone
                        Toast.makeText(requireContext(),"success to load data",Toast.LENGTH_LONG).show()

                    }
                }
            }
        }

    }
}