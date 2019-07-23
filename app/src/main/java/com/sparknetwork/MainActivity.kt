package com.sparknetwork

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sparknetwork.ui.imageList.UploadImageFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, UploadImageFragment.newInstance())
            .commitAllowingStateLoss()
    }
}