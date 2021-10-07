package com.nishimura.cvshealthcodechallenge.network

object FlickrRepositoryImpl : FlickrRepository {
    override suspend fun getImages(tags: String) = RetrofitClient.flickrService.listRepos(tags)
}