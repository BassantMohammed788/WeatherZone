package com.example.weatheapp.alert.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatheapp.models.RepositoryInterface


class AlertViewModelFactory(private val repo: RepositoryInterface) : ViewModelProvider.Factory
{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertViewModel::class.java)){
            AlertViewModel(repo) as T
        }else{
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}