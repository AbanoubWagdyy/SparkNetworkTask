package com.sparknetwork.model

import com.google.gson.annotations.SerializedName

data class DataModel(@SerializedName("name") var name: String) {

    override fun toString(): String {
        return "$name"
    }
}