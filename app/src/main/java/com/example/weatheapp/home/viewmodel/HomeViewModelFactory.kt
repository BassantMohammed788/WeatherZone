package com.example.weatheapp.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatheapp.models.RepositoryInterface

class HomeViewModelFactory(private val repo: RepositoryInterface) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(repo) as T
        }else{
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}