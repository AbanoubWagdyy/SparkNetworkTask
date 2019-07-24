package com.sparknetwork.ui.uploads

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sparknetwork.R

class UploadsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_images)

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, UploadImageFragment.newInstance())
            .commitAllowingStateLoss()
    }
}