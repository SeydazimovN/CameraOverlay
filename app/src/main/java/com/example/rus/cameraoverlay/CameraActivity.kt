package com.example.rus.cameraoverlay

import android.hardware.Camera
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Surface
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera)

        camera_preview.addView(CameraPreview(this), 0)
    }

}
