package com.example.weatheapp.favourite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatheapp.database.FavWeatherEntity
import com.example.weatheapp.database.LocalFavState
import com.example.weatheapp.models.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


class FavouriteViewModel (private val repo: RepositoryInterface): ViewModel() {

    private val mutableStateFlow : MutableStateFlow<LocalFavState> = MutableStateFlow (LocalFavState.Loading)

    val weather: StateFlow<LocalFavState> = mutableStateFlow


    fun getFavWeatherFromRoom() {
        viewModelScope.launch(Dispatchers.IO){
             repo.getFavWeather().catch {
                    e-> mutableStateFlow.value = LocalFavState.Failure(e)
            }.collect {
                    d -> mutableStateFlow.value = LocalFavState.Success(d)
            }
        }
    }

    fun insertFavWeatherIntoRoom(favWeather: FavWeatherEntity){
        viewModelScope.launch(Dispatchers.IO){
            repo.insertFavWeather(favWeather)
        }
    }
    fun deleteFavWeatherFromRoom(favWeather: FavWeatherEntity){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteFavWeather(favWeather)
            getFavWeatherFromRoom()
        }
    }

}