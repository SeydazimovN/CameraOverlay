package com.example.rus.cameraoverlay

import android.content.Context
import android.hardware.Camera
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * Created by RUS on 13.01.2017.
 */
class CameraPreview(context: Context, val camera: Camera) : SurfaceView(context), SurfaceHolder.Callback {

    init {
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        val parameters = camera.parameters
        parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
        camera.parameters = parameters

        camera.setPreviewDisplay(holder)
        camera.startPreview()


    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        if (holder.surface == null) {
            return
        }

        camera.stopPreview()
        camera.setPreviewDisplay(holder)
        camera.startPreview()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        camera.stopPreview()
        camera.release()
    }

}