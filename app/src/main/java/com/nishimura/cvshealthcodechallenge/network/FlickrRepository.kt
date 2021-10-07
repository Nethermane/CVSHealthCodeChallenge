package com.nishimura.cvshealthcodechallenge.network

import com.nishimura.cvshealthcodechallenge.model.FlickrResponse

interface FlickrRepository {
    suspend fun getImages(tags: String = ""): FlickrResponse
}