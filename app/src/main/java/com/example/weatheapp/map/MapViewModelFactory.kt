package com.example.weatheapp.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatheapp.map.viewmodel.MapViewModel
import com.example.weatheapp.models.RepositoryInterface


class MapViewModelFactory(private val repo: RepositoryInterface) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MapViewModel::class.java)){
            MapViewModel(repo) as T
        }else{
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}