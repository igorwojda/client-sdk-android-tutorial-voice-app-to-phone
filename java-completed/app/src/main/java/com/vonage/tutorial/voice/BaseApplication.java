package com.vonage.tutorial.voice;

import android.app.Application;
import com.nexmo.client.BuildConfig;
import com.nexmo.client.NexmoClient;
import timber.log.Timber;

public final class BaseApplication extends Application {

    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        this.initializeNexmoClient();
    }

    private final void initializeNexmoClient() {
        // Init the NexmoClient. You can retrieve NexmoClient instance latter by using NexmoClient.get()
        new NexmoClient.Builder().build(this);
    }
}