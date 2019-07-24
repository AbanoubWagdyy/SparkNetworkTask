package com.sparknetwork.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.sparknetwork.R
import com.sparknetwork.viewModel.UploadImagesViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSION_CODE: Int = 100
    private val PERMISSION_CODE: Int = 100

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
        mViewModel.init()
    }

    private fun initUI() {
        btnChoose.setOnClickListener {
            ImagePicker.with(this)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 101) {
                val fileUri = data?.data
                if (fileUri != null) {
                    val imageStream = fileUri?.let { contentResolver.openInputStream(it) }
                    selectedImage = BitmapFactory.decodeStream(imageStream)
                    ivPhoto.visibility = View.VISIBLE
                    ivPhoto.setImageBitmap(selectedImage)
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CODE &&
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
}