package com.nishimura.cvshealthcodechallenge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nishimura.cvshealthcodechallenge.preferences.UserPreferenceRepository
import com.nishimura.cvshealthcodechallenge.model.FlickrResponse
import com.nishimura.cvshealthcodechallenge.model.Item
import com.nishimura.cvshealthcodechallenge.network.FlickrRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FlickrImageViewModel @Inject constructor(
    application: Application,
    private val repository: FlickrRepository
) : AndroidViewModel(application) {

    //Emits the images to display a list of images
    private val _flickrImages = MutableStateFlow<FlickrResponse?>(null)
    val flickrImages: StateFlow<FlickrResponse?> = _flickrImages

    //Emits whether or not the search is being done
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    //Emits searching history
    private val _savedSearches = MutableStateFlow<List<String>>(emptyList())
    val savedSearches: StateFlow<List<String>> = _savedSearches

    //Largely just for maintaining state when rotating or coming back from details screen
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
            _flickrImages.value = repository.getImages(_currentSearch.value)
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