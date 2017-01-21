package com.example.rus.cameraoverlay

import android.content.Intent
import android.graphics.Bitmap
import android.hardware.Camera
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.io.FileOutputStream

class CameraActivity : AppCompatActivity(), CameraPreview.ImageCapturedListener {

    lateinit var camera: Camera

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera)

        val cameraPreview = CameraPreview(this)

        camera_preview.addView(cameraPreview, 0)
        button_capture.setOnClickListener {
            cameraPreview.takePicture(this)
        }
    }

    override fun onImageCaptured(bitmap: Bitmap) {
        saveImage(bitmap)
    }

    private fun saveImage(finalBitmap: Bitmap) {
        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val photoDir = File(picturesDir, "CameraOverlay")
        if (!photoDir.exists()) {
            photoDir.mkdirs()
        }
        val outputFile = File(photoDir, "${System.currentTimeMillis()}.jpg")
        val out = FileOutputStream(outputFile)
        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        out.flush()
        out.close()
        val path = outputFile.path
        val intent = Intent()
        intent.putExtras(Bundle().apply {
            putString(EXTRA_PHOTO, path)
        })
        setResult(RESULT_OK, intent)
        finish()

    }

    companion object {
        val RESULT_OK = 1
        val EXTRA_PHOTO = "EXTRA_PHOTO"
    }

}
