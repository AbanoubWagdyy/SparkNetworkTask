package com.sparknetworktask.android.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.sparknetworktask.android.model.ImageModel
import com.sparknetworktask.android.model.UploadProgress
import com.sparknetworktask.android.repository.UploadImagesRepository

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