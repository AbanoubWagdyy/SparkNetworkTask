package com.sparknetworktask.android.ui.chooseAndUpload

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.sparknetworktask.android.R
import com.sparknetworktask.android.model.Status
import com.sparknetworktask.android.model.UploadProgress
import com.sparknetworktask.android.viewModel.UploadImagesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import com.sparknetworktask.android.ui.uploads.GalleryActivity

class ChooseAndUploadActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private var mCurrentFileUri: Uri? = null

    private val REQUEST_PERMISSION_CODE: Int = 100
    private val REQUEST_IMAGE: Int = 200

    private var selectedImage: Bitmap? = null
    private lateinit var mViewModel: UploadImagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPermissions()
    }

    private fun initPermissions() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            var permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            requestPermissions(permissions, REQUEST_PERMISSION_CODE)
        } else {
            initViewModelObserver()
            initUI()
        }
    }

    private fun initViewModelObserver() {
        mViewModel = ViewModelProviders.of(this).get(UploadImagesViewModel::class.java)
    }

    private fun initUI() {
        btnChoose.setOnClickListener {
            ImagePicker.with(this)
//                .compress(1024)
//                .maxResultSize(1080, 1080)
                .cropSquare()
                .start(REQUEST_IMAGE)
        }

        btnUpload.setOnClickListener {
            if (mCurrentFileUri != null) {
                progressDialog = ProgressDialog(this@ChooseAndUploadActivity)
                progressDialog.setTitle("Uploading")
                progressDialog.setCancelable(false)
                progressDialog.show()

                val observer = Observer<UploadProgress> { uploadProgress ->
                    updateProgress(uploadProgress)
                }

                mViewModel.uploadImage(mCurrentFileUri!!,getFileExtension(mCurrentFileUri!!)).observe(this, observer)
            }else{
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Please choose valid image !.",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        tvViewUploads.setOnClickListener {
            openGalleryScreen()
        }
    }

    private fun openGalleryScreen() {
        val intent = Intent(this@ChooseAndUploadActivity, GalleryActivity::class.java)
        startActivity(intent)
    }

    private fun updateProgress(
        uploadProgress: UploadProgress
    ) {
        if (uploadProgress.status == Status.PROGRESS) {
            this@ChooseAndUploadActivity.runOnUiThread {
                val progress = uploadProgress.progress
                progressDialog.setMessage("Uploading ($progress) %...")
            }
        } else {
            progressDialog.dismiss()
            if (uploadProgress.status == Status.SUCCESS) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    getString(R.string.success_image_upload),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    getString(R.string.failure_image_upload),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        handlePermissionResult(requestCode, grantResults)
    }

    private fun handlePermissionResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSION_CODE &&
            grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            initViewModelObserver()
            initUI()
        } else {
            Snackbar.make(
                findViewById(android.R.id.content),
                "We need to access external storage to complete your flow !.",
                Snackbar.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE) {
                mCurrentFileUri = data?.data
                val imageStream = contentResolver.openInputStream(mCurrentFileUri!!)
                selectedImage = BitmapFactory.decodeStream(imageStream)
                ivPhoto.visibility = View.VISIBLE
                ivPhoto.setImageBitmap(selectedImage)
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        }
    }

    fun getFileExtension(uri: Uri): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }
}