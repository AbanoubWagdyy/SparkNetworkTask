package com.sparknetwork.model

import com.google.gson.annotations.SerializedName

data class ImageModel(
    @SerializedName("data") val data: List<DataModel>) {
}