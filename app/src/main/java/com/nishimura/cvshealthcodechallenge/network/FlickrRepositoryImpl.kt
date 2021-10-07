package com.nishimura.cvshealthcodechallenge.network

class FlickrRepositoryImpl : FlickrRepository {
    override suspend fun getImages(tags: String) = RetrofitClient.flickrService.listRepos(tags)
}