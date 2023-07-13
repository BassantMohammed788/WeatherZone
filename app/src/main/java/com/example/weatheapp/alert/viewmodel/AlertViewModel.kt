package com.example.weatheapp.alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatheapp.database.AlertWeatherEntity
import com.example.weatheapp.database.LocalAlertState
import com.example.weatheapp.models.RepositoryInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AlertViewModel (private val repo: RepositoryInterface): ViewModel() {

    private val mutableStateFlow : MutableStateFlow<LocalAlertState> = MutableStateFlow (LocalAlertState.Loading)

    val alert: StateFlow<LocalAlertState> = mutableStateFlow

    fun getAlertWeatherFromRoom() {
        viewModelScope.launch(Dispatchers.IO){
            repo.getAlertWeather().catch {
                    e-> mutableStateFlow.value = LocalAlertState.Failure(e)
            }.collect {
                    d -> mutableStateFlow.value = LocalAlertState.Success(d)
            }
        }
    }

    fun deleteALertWeatherFromRoom(alertWeather: AlertWeatherEntity){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteAlertWeather(alertWeather)
            getAlertWeatherFromRoom()
        }
    }
    fun insertALertWeatherFromRoom(alertWeather: AlertWeatherEntity){
        viewModelScope.launch(Dispatchers.IO){
            repo.insertAlertWeather(alertWeather)
        }
    }
}