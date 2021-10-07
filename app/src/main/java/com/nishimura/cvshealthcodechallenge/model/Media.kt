package com.nishimura.cvshealthcodechallenge.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    @SerializedName("m")
    val mediaString: String
) : Parcelable