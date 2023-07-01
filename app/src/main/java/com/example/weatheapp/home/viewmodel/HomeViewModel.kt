package com.example.weatheapp.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatheapp.model.RepositoryInterface
import com.example.weatheapp.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel (private val repo: RepositoryInterface): ViewModel() {


    private val mutableStateFlow : MutableStateFlow<ApiState> = MutableStateFlow (ApiState.Loading)

    val weather: StateFlow<ApiState> = mutableStateFlow


    fun getAllProductOverNetwork(lat: Double, lon: Double, units: String, lang:String) {
        viewModelScope.launch(Dispatchers.IO){
            repo.getWeatherOverNetwork(lat,lon,units,lang).catch {
                    e-> mutableStateFlow.value = ApiState.Failure(e)
            }.collect {
                    d -> mutableStateFlow.value = ApiState.Success(d)
            }
        }
    }

}
