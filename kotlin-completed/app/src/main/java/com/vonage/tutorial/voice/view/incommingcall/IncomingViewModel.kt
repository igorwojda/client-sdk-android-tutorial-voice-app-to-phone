package com.vonage.tutorial.voice.view.incommingcall

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nexmo.client.NexmoCall
import com.nexmo.client.NexmoClient
import com.nexmo.client.request_listener.NexmoApiError
import com.nexmo.client.request_listener.NexmoRequestListener
import com.vonage.tutorial.voice.extension.asLiveData
import com.vonage.tutorial.voice.util.CallManager
import com.vonage.tutorial.voice.util.NavManager

class IncomingViewModel : ViewModel() {
    private val client = NexmoClient.get()
    private val callManager = CallManager
    private val navManager = NavManager

    private val toastMutableLiveData = MutableLiveData<String>()
    val toastLiveData = toastMutableLiveData.asLiveData()

    private val answerCallListener = object : NexmoRequestListener<NexmoCall> {
        override fun onSuccess(call: NexmoCall?) {
            val navDirections =
                IncomingCallFragmentDirections.actionIncomingCallFragmentToOnCallFragment()
            navManager.navigate(navDirections)
        }

        override fun onError(apiError: NexmoApiError) {
            toastMutableLiveData.postValue(apiError.message)
        }
    }

    fun hangup() {
        hangupInternal(true)
    }

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
}