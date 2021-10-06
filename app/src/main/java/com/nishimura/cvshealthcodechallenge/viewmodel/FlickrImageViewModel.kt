package com.nishimura.cvshealthcodechallenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nishimura.cvshealthcodechallenge.model.FlickrImage
import com.nishimura.cvshealthcodechallenge.model.FlickrResponse
import com.nishimura.cvshealthcodechallenge.model.Item
import com.nishimura.cvshealthcodechallenge.network.FlickrRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FlickrImageViewModel: ViewModel() {
    private val _flickrImages = MutableStateFlow<FlickrResponse?>(null)
    val flickrImages: StateFlow<FlickrResponse?> = _flickrImages
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    init {
        searchForImages()
    }
    fun searchForImages(tags: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _flickrImages.value = FlickrRepository.getImages(tags)
            _isLoading.value = false
        }
    }

    /**
     * This really should be done through a room db but the overhead to set it up when no other
     * functionality is required using it means I will be doing it this way instead.
     */
    fun getItemByIndex(index: Int): Item? = _flickrImages.value?.items?.getOrNull(index)
}