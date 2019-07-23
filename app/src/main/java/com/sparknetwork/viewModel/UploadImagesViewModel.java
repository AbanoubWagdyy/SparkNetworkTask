package com.sparknetwork.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.sparknetwork.model.ImageModel;
import com.sparknetwork.repository.UploadImagesRepository;

/**
 * Created by kamal on 8/2/18.
 */

public class UploadImagesViewModel extends ViewModel {

    private MutableLiveData<ImageModel> data;

    private UploadImagesRepository movieModel;

    public UploadImagesViewModel() {
        movieModel = new UploadImagesRepository();
    }

    public void init() {
        if (this.data != null) {
            return;
        }
        data = movieModel.getImages();
    }

    public MutableLiveData<ImageModel> getImages() {
        return this.data;
    }
}
