package com.nishimura.cvshealthcodechallenge.network

object FlickrRepository {
    suspend fun getImages(tags: String = "") = RetrofitClient.flickrService.listRepos(tags)
}