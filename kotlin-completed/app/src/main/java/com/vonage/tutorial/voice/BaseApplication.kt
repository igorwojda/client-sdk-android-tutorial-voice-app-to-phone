package com.vonage.tutorial.voice

import android.app.Application
import com.nexmo.client.BuildConfig
import com.nexmo.client.NexmoClient
import timber.log.Timber

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        initializeNexmoClient()
    }

    private fun initializeNexmoClient() {
        NexmoClient.Builder().build(this)
    }
}
