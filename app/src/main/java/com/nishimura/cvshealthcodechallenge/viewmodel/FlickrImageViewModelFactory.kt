package com.nishimura.cvshealthcodechallenge.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nishimura.cvshealthcodechallenge.network.FlickrRepository

class FlickrImageViewModelFactory(
    private val application: Application,
    private val repository: FlickrRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlickrImageViewModel::class.java)) {
            return FlickrImageViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}