package com.sparknetwork.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.sparknetwork.model.ImageModel
import com.sparknetwork.model.UploadProgress
import com.sparknetwork.repository.UploadImagesRepository

class UploadImagesViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private lateinit var mRepository: UploadImagesRepository

    fun uploadImage(uri: Uri, mimeType: String?): MutableLiveData<UploadProgress> {
        mRepository = UploadImagesRepository(context)
        return mRepository.uploadImage(uri, mimeType)
    }

    fun getImages(): MutableLiveData<List<ImageModel>> {
        mRepository = UploadImagesRepository(context)
        return mRepository.getImageFiles()
    }
}