package com.nishimura.cvshealthcodechallenge.network

import com.nishimura.cvshealthcodechallenge.model.FlickrImage
import com.nishimura.cvshealthcodechallenge.model.FlickrResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface FlickrService {
    @GET("services/feeds/photos_public.gne")
    suspend fun listRepos(@Query("tags") tags: String?,
                          @Query("format") format: String? = "json",
                          @Query("nojsoncallback") noJsonCallback: Int = 1,
                          ): FlickrResponse
}
