package com.sparknetworktask.android.repository


import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sparknetworktask.android.model.Constants
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.sparknetworktask.android.model.UploadProgress
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.UploadTask
import com.sparknetworktask.android.model.ImageModel
import java.util.*
import com.google.android.gms.tasks.OnCompleteListener
import com.sparknetworktask.android.model.Status


class UploadImagesRepository(val mContext: Context) {

    private lateinit var uploadProgressLiveData: MutableLiveData<UploadProgress>
    private lateinit var imagesLiveData: MutableLiveData<List<ImageModel>>

    private var mStorageReference: StorageReference? = null
    private var mDatabase: DatabaseReference? = null

    init {
        mStorageReference = FirebaseStorage.getInstance().getReference(Constants.STORAGE_PATH_UPLOADS)
        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS)
    }

    fun uploadImage(uri: Uri, mimeType: String?): MutableLiveData<UploadProgress> {

        uploadProgressLiveData = MutableLiveData()

        val uniqueId = UUID.randomUUID().toString()

        val ur_firebase_reference = mStorageReference!!.child("uploads/$uniqueId")

        val uploadTask = ur_firebase_reference.putFile(uri)

        uploadProgressLiveData.value = UploadProgress(0, Status.PROGRESS)

        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                throw task.exception!!
            }
            uploadProgressLiveData.value = UploadProgress(50, Status.PROGRESS)
            ur_firebase_reference.downloadUrl
        })
            .addOnCompleteListener(OnCompleteListener<Uri> { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val upload =
                        ImageModel(uniqueId, downloadUri.toString())
                    val uploadId = mDatabase!!.push().key
                    mDatabase!!.child(uploadId!!).setValue(upload)
                    uploadProgressLiveData.value = UploadProgress(0, Status.SUCCESS)
                } else {
                    uploadProgressLiveData.value = UploadProgress(0, Status.FAILURE)
                }
            })

        return uploadProgressLiveData
    }

    fun getImageFiles(): MutableLiveData<List<ImageModel>> {

        imagesLiveData = MutableLiveData()

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS)

        mDatabase!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var uploads = mutableListOf<ImageModel>()
                for (postSnapshot in snapshot.children) {
                    val upload = postSnapshot.getValue(ImageModel::class.java)
                    uploads.add(upload!!)
                }
                imagesLiveData.value = uploads
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("", "")
            }
        })

        return imagesLiveData
    }
}