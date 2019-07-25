package com.sparknetwork.repository


import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sparknetwork.model.Constants
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.annotation.NonNull
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.sparknetwork.model.UploadProgress
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.UploadTask
import com.sparknetwork.model.ImageModel
import com.sparknetwork.model.Status
import java.lang.Exception
import java.util.*


class UploadImagesRepository(val mContext: Context) {

    private lateinit var uuid: String
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

        uuid = UUID.randomUUID().toString()

        FirebaseStorage.getInstance().maxUploadRetryTimeMillis = 2000

        mStorageReference = FirebaseStorage.getInstance().reference

        mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        uuid = UUID.randomUUID().toString()

        val sRef = mStorageReference!!.child(
            Constants.STORAGE_PATH_UPLOADS + uuid
        )

        var activeUploadTasks = sRef.activeUploadTasks

        for (task in activeUploadTasks) {
            task.cancel()
        }

        val uploadTask = sRef.putFile(uri)

        uploadTask
            .addOnSuccessListener { taskSnapshot ->
                val ref = mStorageReference!!.child("{${Constants.STORAGE_PATH_UPLOADS}}/$uuid")
                uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot,
                        Task<Uri>> { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    return@Continuation ref.downloadUrl
                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val upload =
                            ImageModel(uuid, task.result.toString())

                        val uploadId = mDatabase!!.push().key
                        mDatabase!!.child(uploadId!!).setValue(upload)
                        uploadProgressLiveData.value = UploadProgress(0, Status.SUCCESS)
                    } else {
                        uploadProgressLiveData.value = UploadProgress(0, Status.FAILURE)
                    }
                }
            }
            .addOnProgressListener { taskSnapshot ->
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                uploadProgressLiveData.value = UploadProgress(progress.toInt(), Status.PROGRESS)
            }
            .addOnFailureListener {
                uploadProgressLiveData.value = UploadProgress(0, Status.FAILURE)
            }

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