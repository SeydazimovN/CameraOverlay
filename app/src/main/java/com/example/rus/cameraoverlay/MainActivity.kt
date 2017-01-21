package com.example.rus.cameraoverlay

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_open_camera.setOnClickListener { startActivityForResult(Intent(this, CameraActivity::class.java), 1) }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == CameraActivity.RESULT_OK) {
            if (data != null) {
                val path = data.extras[CameraActivity.EXTRA_PHOTO] as String
                imv_photo.setImageBitmap(BitmapFactory.decodeFile(path, BitmapFactory.Options().apply {
                    inSampleSize = 8
                }))
            }
        }
    }
}
