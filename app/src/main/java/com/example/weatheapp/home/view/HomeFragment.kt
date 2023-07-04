package com.example.weatheapp.home.view

import MyLocation
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatheapp.MySharedPreferences
import com.example.weatheapp.R
import com.example.weatheapp.database.ConcreteLocalSource
import com.example.weatheapp.databinding.FragmentHomeBinding
import com.example.weatheapp.favourite.view.FavouriteFragment
import com.example.weatheapp.home.viewmodel.HomeViewModel
import com.example.weatheapp.home.viewmodel.HomeViewModelFactory
import com.example.weatheapp.model.Repository
import com.example.weatheapp.network.ApiClient
import com.example.weatheapp.network.ApiState
import com.example.weatheapp.utilities.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    val location = MyLocation
    lateinit var homeViewModelFactory: HomeViewModelFactory
    lateinit var homeViewModel: HomeViewModel
    lateinit var dailyWeatherAdapter: DailyWeatherAdapter
    lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter
    lateinit var unit: String
    lateinit var windUnit: String
     var lat:Double = 2.0
     var lng:Double = 2.1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mySharedPreferences = MySharedPreferences.getInstance(requireContext())
        val lang = mySharedPreferences.getLanguagePreference()
        unit = mySharedPreferences.getTempratureUnitPreference().toString()
        windUnit = mySharedPreferences.getWindSpeedPreference().toString()
        if (mySharedPreferences.getHomeDestination()==Constants.FAVOURITE.toString()){
             lat = mySharedPreferences.getFavLat()!!.toDouble()
            lng = mySharedPreferences.getFavLng()!!.toDouble()
            mySharedPreferences.saveHomeDestination(Constants.HOME.toString())
            binding.topbarTV.text = "${context?.getString(R.string.favourite)}"
            val navView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavBar)
            navView?.visibility = View.GONE
        }else{
            lat = location.lat!!
            lng = location.lng!!
            val navView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavBar)
            navView?.visibility = View.VISIBLE
        }

        homeViewModelFactory = HomeViewModelFactory(Repository.getInstance(ApiClient.getInstance(),ConcreteLocalSource(requireContext())))

        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        dailyWeatherAdapter = DailyWeatherAdapter(mySharedPreferences)
        binding.homeDailyRecycler.adapter = dailyWeatherAdapter
        binding.homeDailyRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        hourlyWeatherAdapter = HourlyWeatherAdapter(mySharedPreferences)
        binding.homeHourlyRecycler.adapter = hourlyWeatherAdapter
        binding.homeHourlyRecycler.layoutManager =  LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        lifecycleScope.launch {
            homeViewModel.getAllProductOverNetwork(lat, lng, unit!!, lang!!)
            homeViewModel.weather.collectLatest { result ->
                when (result) {
                    is ApiState.Loading -> {
                        binding.homeProgreesBar.visibility=View.VISIBLE
                        binding.homeConstraintLayout.visibility=View.GONE
                    }
                    is ApiState.Failure -> {
                        Log.i("TAG", "onCreate: failed")
                        Toast.makeText(requireContext(), "failed to load data", Toast.LENGTH_LONG)
                            .show()
                    }
                    is ApiState.Success -> {
                        binding.homeProgreesBar.visibility=View.GONE
                        binding.homeConstraintLayout.visibility=View.VISIBLE
                        binding.homeDateTv2.text = getCurrentDate(lang)
                        binding.homeDegreeTv.text = "${result.weather.current.temp} ${getTemperatureUnit(requireContext(), unit)}"
                        binding.homeWindTv.text = convertWindUnit(result.weather.current.wind_speed)
                        binding.homePressyreTv.text = "${result.weather.current.pressure} ${context?.getString(R.string.hpa)}"
                        binding.homeHumidityTv.text = "${result.weather.current.humidity} %"
                        binding.homeVisibilityTv.text = "${result.weather.current.visibility} ${context?.getString(R.string.visbilitym)}"
                        binding.homeUltraTv.text = "${result.weather.current.uvi}"
                        binding.homeCloudTv.text = "${result.weather.current.clouds} %"
                        binding.homeCityTv.text = getCountryName(requireContext(), lang)
                        binding.homeWeatherDescTv.text =
                            result.weather.current.weather[0].description
                        val drawableResId = getIconResource(
                            result.weather.current.weather[0].icon,
                            requireContext()
                        )
                        Log.i("resu", "onViewCreated: ${result.weather.current.weather[0].icon}")
                        binding.homeWeatherIcon.setImageResource(drawableResId)
                        dailyWeatherAdapter.submitList(result.weather.daily)
                        hourlyWeatherAdapter.submitList(result.weather.hourly)
                    }
                }
            }
        }
    }


    fun convertWindUnit(windSpeed: Double): String {
        var wind: String = windSpeed.toString()
        if (unit == Constants.metric.toString() && windUnit == Constants.mile.toString()) {
            wind = "${(windSpeed * 2.23694).toFloat()} ${context?.getString(R.string.mile)}"
        } else if (unit == Constants.metric.toString() && windUnit == Constants.metr.toString()) {
            wind = "$windSpeed ${context?.getString(R.string.metr)}"
        } else if (unit == Constants.standard.toString() && windUnit == Constants.mile.toString()) {
            wind = "${(windSpeed * 0.44704).toFloat()} ${context?.getString(R.string.mile)}"
        } else if (unit == Constants.standard.toString() && windUnit == Constants.metr.toString()) {
            wind = "$windSpeed ${context?.getString(R.string.metr)}"
        } else if (unit == Constants.imperial.toString() && windUnit == Constants.mile.toString()) {
            wind = "$windSpeed ${context?.getString(R.string.mile)}"
        } else if (unit == Constants.imperial.toString() && windUnit == Constants.metr.toString()) {
            wind = "${(windSpeed * 0.44704).toFloat()} ${context?.getString(R.string.metr)}"
        }
        return wind
    }

    fun getCountryName(context: Context, languageCode: String): String? {
        val geocoder = Geocoder(context, Locale(languageCode))
        val addresses = geocoder.getFromLocation(lat, lng, 1)
        if (addresses!!.isNotEmpty()) {
            val address = addresses[0]
            return "${address.adminArea}"
        }
        return null
    }

    fun getCurrentDate(languageCode: String): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEE d MMM", Locale(languageCode))
        return dateFormat.format(calendar.time)
    }
    override fun onDestroy() {
        super.onDestroy()
            val navView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavBar)
            navView.visibility = View.VISIBLE
    }

}