package com.vonage.tutorial.voice.view.main

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nexmo.client.NexmoCall
import com.nexmo.client.NexmoClient
import com.nexmo.client.request_listener.NexmoApiError
import com.nexmo.client.request_listener.NexmoRequestListener
import com.vonage.tutorial.voice.extension.asLiveData
import com.vonage.tutorial.voice.util.CallManager
import com.vonage.tutorial.voice.util.NavManager
import timber.log.Timber

class MainViewModel : ViewModel() {

    private val client = NexmoClient.get()
    private val callManager = CallManager
    private val navManager = NavManager

    private val toastMutableLiveData = MutableLiveData<String>()
    val toastLiveData = toastMutableLiveData.asLiveData()

    private val loadingMutableLiveData = MutableLiveData<Boolean>()
    val loadingLiveData = loadingMutableLiveData.asLiveData()

    private val _currentUserNameMutableLiveData = MutableLiveData<String>()
    val currentUserNameLiveData = _currentUserNameMutableLiveData.asLiveData()

    private val callListener = object : NexmoRequestListener<NexmoCall> {
        override fun onSuccess(call: NexmoCall?) {
            callManager.onGoingCall = call

            loadingMutableLiveData.postValue(false)

            val navDirections = MainFragmentDirections.actionMainFragmentToOnCallFragment()
            navManager.navigate(navDirections)
        }

        override fun onError(apiError: NexmoApiError) {
            Timber.e(apiError.message)
            toastMutableLiveData.postValue(apiError.message)
            loadingMutableLiveData.postValue(false)
        }
    }

    fun onInit(mainFragmentArg: MainFragmentArgs) {
        val currentUserName = mainFragmentArg.userName
        _currentUserNameMutableLiveData.postValue(currentUserName)
    }

    override fun onCleared() {
        client.removeIncomingCallListeners()
    }

    @SuppressLint("MissingPermission")
    fun startAppToPhoneCall() {
        TODO("Fill be body for app to phone call tutorial")
    }

    fun onBackPressed() {
        client.logout()
    }
}
