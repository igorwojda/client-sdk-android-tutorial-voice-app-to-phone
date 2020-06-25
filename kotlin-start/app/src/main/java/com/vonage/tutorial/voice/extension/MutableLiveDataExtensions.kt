package com.vonage.tutorial.voice.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

@Suppress("UnsafeCast")
fun <T> MutableLiveData<T>.asLiveData() = this as LiveData<T>
