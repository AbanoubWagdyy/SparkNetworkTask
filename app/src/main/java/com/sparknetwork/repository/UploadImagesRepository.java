package com.sparknetwork.repository;


import androidx.lifecycle.MutableLiveData;
import com.sparknetwork.model.ImageModel;

public class UploadImagesRepository {

    public UploadImagesRepository() {
    }

    public MutableLiveData<ImageModel> getImages() {
        MutableLiveData<ImageModel> refferAndInvitePojoMutableLiveData = new MutableLiveData<>();
//        apiInterface = ApiClient.getClientAuthentication().create(APIInterface.class);
//        Call<ImageModel> call = apiInterface.getdata();
//        call.enqueue(new Callback<ImageModel>() {
//            @Override
//            public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {
//                if (response.body() != null) {
//                    refferAndInvitePojoMutableLiveData.setValue(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ImageModel> call, Throwable t) {
//
//            }
//        });
        return refferAndInvitePojoMutableLiveData;
    }
}
