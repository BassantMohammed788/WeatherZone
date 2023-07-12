package com.example.weatheapp.favourite.view

import android.app.AlertDialog
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatheapp.MySharedPreferences
import com.example.weatheapp.database.ConcreteLocalSource
import com.example.weatheapp.database.FavWeatherEntity
import com.example.weatheapp.database.LocalFavState
import com.example.weatheapp.databinding.FragmentFavouriteBinding
import com.example.weatheapp.favourite.viewmodel.FavouriteViewModel
import com.example.weatheapp.favourite.viewmodel.FavouriteViewModelFactory
import com.example.weatheapp.models.Repository
import com.example.weatheapp.network.WeatherClient
import com.example.weatheapp.utilities.Constants
import com.example.weatheapp.utilities.isConnected
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import com.example.weatheapp.R

class FavouriteFragment : Fragment() {
    lateinit var binding: FragmentFavouriteBinding
    lateinit var mySharedPreferences: MySharedPreferences
    lateinit var favouriteViewModel: FavouriteViewModel
    lateinit var faViewModelFactory: FavouriteViewModelFactory
    lateinit var favouriteWeatherAdapter: FavouriteWeatherAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        mySharedPreferences = MySharedPreferences.getInstance(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        faViewModelFactory = FavouriteViewModelFactory(
            Repository.getInstance(
                WeatherClient.getInstance(),
                ConcreteLocalSource(requireContext())
            )
        )

        favouriteViewModel =
            ViewModelProvider(this, faViewModelFactory).get(FavouriteViewModel::class.java)

        favouriteWeatherAdapter =
            FavouriteWeatherAdapter(mySharedPreferences, openFavDetailsLambda, deleteLambda)

        recyclerView = binding.favouriteRecycler
        recyclerView.apply {
            adapter = favouriteWeatherAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        lifecycleScope.launch {
            favouriteViewModel.getFavWeatherFromRoom()
            favouriteViewModel.weather.collectLatest { result ->
                when (result) {
                    is LocalFavState.Loading -> {
                        binding.FavouriteIcon.visibility = View.VISIBLE
                        binding.noAlertTv.visibility = View.VISIBLE
                        binding.favouriteRecycler.visibility = View.GONE
                    }
                    is LocalFavState.Success -> {
                        if (result.favWeather.isEmpty()) {
                            binding.FavouriteIcon.visibility = View.VISIBLE
                            binding.noAlertTv.visibility = View.VISIBLE
                            binding.favouriteRecycler.visibility = View.GONE
                        } else {
                            binding.FavouriteIcon.visibility = View.GONE
                            binding.noAlertTv.visibility = View.GONE
                            binding.favouriteRecycler.visibility = View.VISIBLE
                            favouriteWeatherAdapter.submitList(result.favWeather)
                        }
                    }
                    is LocalFavState.Failure -> {
                        Log.i("TAG", "onCreate: failed")
                        Toast.makeText(requireContext(), "failed to load data", Toast.LENGTH_LONG)
                            .show()
                    }

                    else -> {}
                }
            }
        }

        binding.favouriteFAB.setOnClickListener {
            if (isConnected(requireContext())) {
                mySharedPreferences.saveMapDestinationPrefrence(Constants.FAVOURITE.toString())
                val currentView = view
                Navigation.findNavController(currentView!!).navigate(R.id.action_menuNavFavID_to_mapsFragment)

            } else {
                val title = "${context?.getString(R.string.addFavAlertTitle)}"
                val message = "${context?.getString(R.string.addFavAlertMessage)}"
                creatAlert(title, message)
            }
        }
    }

    private val deleteLambda = { favWeather: FavWeatherEntity ->
            favouriteViewModel.deleteFavWeatherFromRoom(favWeather)

    }

    private val openFavDetailsLambda = { favWeather: FavWeatherEntity ->
        val currentView = view // capture the view parameter in a local variable
      //  if (isConnected(requireContext())) {
            mySharedPreferences.saveHomeDestination(Constants.FAVOURITE.toString())
            Navigation.findNavController(currentView!!).navigate(R.id.action_menuNavFavID_to_menuNavHomeID)

   /*     } else {
            val title = "${context?.getString(R.string.addFavAlertTitle)}"
            val message = "${context?.getString(R.string.openFavAlertMessage)}"
            creatAlert(title, message)
        }*/
    }

    fun creatAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("${context?.getString(R.string.ok)}") { dialog, which ->
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

}