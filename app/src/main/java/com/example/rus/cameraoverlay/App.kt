package com.example.rus.cameraoverlay

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics

/**
 * Created by Vladislav Zhukov on 21.01.2017.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        analytics = FirebaseAnalytics.getInstance(this)

    }

    companion object {
        lateinit var analytics: FirebaseAnalytics
    }
}