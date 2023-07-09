package com.example.weatheapp.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatheapp.database.FavWeatherEntity
import com.example.weatheapp.database.LocalFavState
import com.example.weatheapp.models.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MapViewModel (private val repo: RepositoryInterface): ViewModel() {

    private val mutableStateFlow : MutableStateFlow<LocalFavState> = MutableStateFlow (LocalFavState.Loading)

    val weather: StateFlow<LocalFavState> = mutableStateFlow

    fun insertFavWeatherIntoRoom(favWeather: FavWeatherEntity){
        viewModelScope.launch(Dispatchers.IO){
            repo.insertFavWeather(favWeather)
        }
    }
}
