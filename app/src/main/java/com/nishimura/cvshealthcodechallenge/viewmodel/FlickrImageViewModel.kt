package com.nishimura.cvshealthcodechallenge.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishimura.cvshealthcodechallenge.UserPreferenceRepository
import com.nishimura.cvshealthcodechallenge.model.FlickrResponse
import com.nishimura.cvshealthcodechallenge.model.Item
import com.nishimura.cvshealthcodechallenge.network.FlickrRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FlickrImageViewModel(application: Application) : AndroidViewModel(application) {
    private val _flickrImages = MutableStateFlow<FlickrResponse?>(null)
    val flickrImages: StateFlow<FlickrResponse?> = _flickrImages
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _savedSearches = MutableStateFlow<List<String>>(emptyList())
    val savedSearches: StateFlow<List<String>> = _savedSearches

    private val _currentSearch = MutableStateFlow("")
    val currentSearch: StateFlow<String> = _currentSearch

    init {
        searchForImages()
    }

    /**
     * Mainly for storing state between rotation/screen traversal
     */
    fun setCurrentSearch(search: String) {
        _currentSearch.value = search

    }
    fun searchForImages() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _flickrImages.value = FlickrRepository.getImages(_currentSearch.value)
            _isLoading.value = false
            saveSearch()
        }
    }

    /**
     * This really should be done through a room db but the overhead to set it up when no other
     * functionality is required using it means I will be doing it this way instead.
     */
    fun getItemByIndex(index: Int): Item? = _flickrImages.value?.items?.getOrNull(index)

    private fun saveSearch() {
        _currentSearch.value.takeIf { it.isNotBlank() }?.let {

            viewModelScope.launch {
                val wasChanged =
                    UserPreferenceRepository.addSearchToHistory(it, getApplication())
                if (wasChanged) {
                    _savedSearches.value =
                        UserPreferenceRepository.getSearchHistory(getApplication())
                }
            }
        }
    }
    fun getSaveSearches() {
        viewModelScope.launch {
            _savedSearches.value = UserPreferenceRepository.getSearchHistory(getApplication())
        }
    }
}