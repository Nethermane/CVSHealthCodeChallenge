package com.nishimura.cvshealthcodechallenge.model

data class FlickrResponse(
    val description: String,
    val generator: String,
    val items: List<Item>,
    val link: String,
    val modified: String,
    val title: String
)