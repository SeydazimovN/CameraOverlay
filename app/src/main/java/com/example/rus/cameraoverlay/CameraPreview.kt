package com.example.rus.cameraoverlay

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View

/**
 * Created by RUS on 13.01.2017.
 */
@Suppress("DEPRECATION")
class CameraPreview(context: Context, var camera: Camera = Camera.open(), var isCameraOpened: Boolean = true) : SurfaceView(context), SurfaceHolder.Callback {

    interface ImageCapturedListener {
        fun onImageCaptured(bitmap: Bitmap)
    }

    lateinit var previewSize: Camera.Size
    lateinit var pictureSize: Camera.Size

    init {
        holder.addCallback(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = View.resolveSize(suggestedMinimumWidth, widthMeasureSpec)
        val height = View.resolveSize(suggestedMinimumHeight, heightMeasureSpec);
        setMeasuredDimension(width, height)

        previewSize = getOptimalPreviewSize(camera.parameters.supportedPreviewSizes, width, height)
        pictureSize = getOptimalPictureSize(camera.parameters.supportedPictureSizes, width, height)
    }

    fun getOptimalPreviewSize(sizes: List<Camera.Size>, width: Int, height: Int): Camera.Size {
        if (sizes.filter { it.width == width && it.height == height }.isNotEmpty()) {
            return sizes.find { it.width == width && it.height == height }!!
        }

        return sizes.minBy { getEuclidDistance(it.width, it.height, width, height) }!!
    }

    fun getOptimalPictureSize(sizes: List<Camera.Size>, width: Int, height: Int): Camera.Size {
        if (sizes.contains(previewSize)) {
            return previewSize
        }

        if (sizes.filter { it.width == width && it.height == height }.isNotEmpty()) {
            return sizes.find { it.width == width && it.height == height }!!
        }

        return sizes.minBy { getEuclidDistance(it.width, it.height, width, height) }!!
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        if (!isCameraOpened) {
            camera = Camera.open()
            camera.startPreview()
        }

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        val parameters = camera.parameters
        val supportedFocusModes = parameters.supportedFocusModes

        if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
        } else if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
        }

        if (parameters.maxNumFocusAreas != 0) {
            parameters.focusAreas = listOf(Camera.Area(Rect(-100, -100, 100, 100), 1))
        }

        parameters.setPreviewSize(previewSize.width, previewSize.height)
        parameters.setPictureSize(pictureSize.width, pictureSize.height)

        camera.parameters = parameters

        camera.setPreviewDisplay(holder)
        camera.startPreview()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        camera.stopPreview()
        camera.release()
        isCameraOpened = false
    }


    fun takePicture(imageCapturedListener: ImageCapturedListener) {
        camera.takePicture(null, null) { bytes, camera ->
            imageCapturedListener.onImageCaptured(BitmapFactory.decodeByteArray(bytes, 0, bytes.size, BitmapFactory.Options().apply { inPurgeable = true }))
        }
    }

    private fun getEuclidDistance(x1: Int, y1: Int, x2: Int, y2: Int): Double {
        return Math.sqrt((Math.abs(x1 - x2) * Math.abs(x1 - x2) + Math.abs(y1 - y2) * Math.abs(y1 - y2)).toDouble())
    }

}