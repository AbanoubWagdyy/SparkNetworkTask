package com.sparknetwork.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class ImageModel {
    var id: String = ""
    var url: String = ""

    constructor() {

    }

    constructor(id: String, url: String) {
        this.id = id
        this.url = url
    }
}