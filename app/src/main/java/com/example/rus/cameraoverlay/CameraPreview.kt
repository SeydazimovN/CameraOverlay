package com.example.rus.cameraoverlay

import android.content.Context
import android.graphics.Rect
import android.hardware.Camera
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * Created by RUS on 13.01.2017.
 */
class CameraPreview(context: Context, var camera: Camera = Camera.open(), var previewSize: Camera.Size? = null, var isCameraOpened: Boolean = true) : SurfaceView(context), SurfaceHolder.Callback {

    init {
        holder.addCallback(this)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = resolveSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = resolveSize(suggestedMinimumHeight, heightMeasureSpec)
        setMeasuredDimension(width, height)

        if (previewSize == null) {
            previewSize = getOptimalPreviewSize(camera.parameters.supportedPreviewSizes, width, height)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        if (!isCameraOpened) {
            camera = Camera.open()
            isCameraOpened = true
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        val parameters = camera.parameters
        parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
        parameters.focusAreas = listOf(Camera.Area(Rect(-100, -100, 100, 100), 100))
        if (previewSize != null) {
            parameters.setPreviewSize((previewSize as Camera.Size).width, (previewSize as Camera.Size).height)
        }
        parameters.setPictureSize(1920, 1080)
        camera.parameters = parameters
        val supportedPreviewSizes = parameters.supportedPreviewSizes

        camera.setPreviewDisplay(holder)
        camera.startPreview()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        camera.stopPreview()
        camera.release()
        isCameraOpened = false
    }

    private fun getOptimalPreviewSize(sizes: List<Camera.Size>, w: Int, h: Int): Camera.Size? {
        val ASPECT_TOLERANCE = 0.1
        val targetRatio = h.toDouble() / w

        var optimalSize: Camera.Size? = null
        var minDiff = java.lang.Double.MAX_VALUE

        val targetHeight = h

        for (size in sizes) {
            val ratio = size.width.toDouble() / size.height
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size
                minDiff = Math.abs(size.height - targetHeight).toDouble()
            }
        }

        if (optimalSize == null) {
            minDiff = java.lang.Double.MAX_VALUE
            for (size in sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size
                    minDiff = Math.abs(size.height - targetHeight).toDouble()
                }
            }
        }
        return optimalSize
    }

}