package com.vonage.tutorial.voice.view.incommingcall

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nexmo.client.NexmoCall
import com.nexmo.client.request_listener.NexmoApiError
import com.nexmo.client.request_listener.NexmoRequestListener
import com.vonage.tutorial.voice.extension.asLiveData
import com.vonage.tutorial.voice.util.CallManager
import com.vonage.tutorial.voice.util.NavManager
import com.vonage.tutorial.voice.util.observer

class IncomingViewModel : ViewModel() {
    private val callManager = CallManager
    private val navManager = NavManager

    private var otherUserName: String by observer("") {
        _otherUserNameMutableLiveData.postValue(it)
    }

    private val _otherUserNameMutableLiveData = MutableLiveData<String>()
    val otherUserNameLiveData = _otherUserNameMutableLiveData.asLiveData()

    private val toastMutableLiveData = MutableLiveData<String>()
    val toastLiveData = toastMutableLiveData.asLiveData()

    private val answerCallListener = object : NexmoRequestListener<NexmoCall> {
        override fun onSuccess(call: NexmoCall?) {
            val navDirections = IncomingCallFragmentDirections.actionIncomingCallFragmentToOnCallFragment(otherUserName)
            navManager.navigate(navDirections)
        }

        override fun onError(apiError: NexmoApiError) {
            toastMutableLiveData.postValue(apiError.message)
        }
    }

    fun hangup() {
        hangupInternal(true)
    }

    @SuppressLint("MissingPermission")
    fun answer() {
        callManager.onGoingCall?.answer(answerCallListener)
    }

    fun onBackPressed() {
        hangupInternal(false)
    }

    private fun hangupInternal(popBackStack: Boolean) {
        callManager.onGoingCall?.hangup(object : NexmoRequestListener<NexmoCall> {
            override fun onSuccess(call: NexmoCall?) {
                callManager.onGoingCall = null

                if (popBackStack) {
                    navManager.popBackStack()
                }
            }

            override fun onError(apiError: NexmoApiError) {
                toastMutableLiveData.postValue(apiError.message)
            }
        })
    }

    fun onInit(args: IncomingCallFragmentArgs) {
        otherUserName = args.otherUserName
    }
}