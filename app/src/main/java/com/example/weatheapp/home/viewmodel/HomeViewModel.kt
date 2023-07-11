package com.example.weatheapp.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatheapp.database.MyResponseEntity
import com.example.weatheapp.models.RepositoryInterface
import com.example.weatheapp.network.ApiState
import com.example.weatheapp.database.RoomState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel (private val repo: RepositoryInterface): ViewModel() {

    private val mutableStateFlow : MutableStateFlow<ApiState> = MutableStateFlow (ApiState.Loading)

    val weather: StateFlow<ApiState> = mutableStateFlow

    private val homeMutableStateFlow : MutableStateFlow<RoomState> = MutableStateFlow (RoomState.Loading)

    val homeWeather: StateFlow<RoomState> = homeMutableStateFlow

    fun getWeatherOverNetwork(lat: Double, lon: Double, units: String, lang:String) {
        viewModelScope.launch(Dispatchers.IO){
            repo.getWeatherOverNetwork(lat,lon,units,lang).catch {
                    e-> mutableStateFlow.value = ApiState.Failure(e)
            }.collect {
                    d -> mutableStateFlow.value = ApiState.Success(d)
            }
        }
    }

    fun getWeatherFromRoom(type:String,id:String){
        viewModelScope.launch (Dispatchers.IO){
            repo.getSavedWeather(type,id).catch { e -> homeMutableStateFlow.value = RoomState.Failure(e) }
                .collect{ data ->
                    if (data != null){
                        homeMutableStateFlow.value =  RoomState.Success(data)
                    }else {
                        homeMutableStateFlow.value =  RoomState.Failure(Throwable())
                    }
                }
                }
        }
    fun insertWeatherIntoRoom(homeWeather:MyResponseEntity){
        viewModelScope.launch(Dispatchers.IO){
            repo.insertHomeWeather(homeWeather)
            Log.i("TAG", "insertWeatherIntoRoom: inserted ")
            Log.i("TAG", "pojo that inserted: $homeWeather ")

        }
    }
}
