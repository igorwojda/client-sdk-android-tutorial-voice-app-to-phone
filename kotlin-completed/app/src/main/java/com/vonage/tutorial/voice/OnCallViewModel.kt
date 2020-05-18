package com.vonage.tutorial.voice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nexmo.client.NexmoCall
import com.nexmo.client.NexmoCallEventListener
import com.nexmo.client.NexmoCallMember
import com.nexmo.client.NexmoCallMemberStatus
import com.nexmo.client.NexmoClient
import com.nexmo.client.NexmoMediaActionState
import com.nexmo.client.request_listener.NexmoApiError
import com.nexmo.client.request_listener.NexmoRequestListener
import com.vonage.tutorial.voice.extension.asLiveData
import com.vonage.tutorial.voice.util.CallManager
import com.vonage.tutorial.voice.util.NavManager
import timber.log.Timber

class OnCallViewModel : ViewModel() {
    private val client = NexmoClient.get()

    private val callManager = CallManager
    private val navManager = NavManager

    private val toastMutableLiveData = MutableLiveData<String>()
    val toastLiveData = toastMutableLiveData.asLiveData()

    private val callEventListener = object : NexmoCallEventListener {
        override fun onMemberStatusUpdated(nexmoCallStatus: NexmoCallMemberStatus, callMember: NexmoCallMember) {
            Timber.d("CallEventListener.onMemberStatusUpdated: ${callMember.user.name} : $nexmoCallStatus")

            if (nexmoCallStatus == NexmoCallMemberStatus.COMPLETED || nexmoCallStatus == NexmoCallMemberStatus.CANCELED) {
                callManager.onGoingCall = null
                navManager.popBackStack()
            }
        }

        override fun onMuteChanged(nexmoMediaActionState: NexmoMediaActionState, callMember: NexmoCallMember) {
            Timber.d("CallEventListener.onMuteChanged: ${callMember.user.name} : $nexmoMediaActionState")
        }

        override fun onEarmuffChanged(nexmoMediaActionState: NexmoMediaActionState, callMember: NexmoCallMember) {
            Timber.d("CallEventListener.onEarmuffChanged: ${callMember.user.name} : $nexmoMediaActionState")
        }

        override fun onDTMF(dtmf: String, callMember: NexmoCallMember) {
            Timber.d("CallEventListener.onDTMF: ${callMember.user.name} : $dtmf")
        }
    }

    init {
        val onGoingCall = checkNotNull(callManager.onGoingCall) { "Call is null" }
        onGoingCall.addCallEventListener(callEventListener)
    }

    override fun onCleared() {
        super.onCleared()

        callManager.onGoingCall?.removeCallEventListener(callEventListener)
    }

    fun onBackPressed() {
        hangupInternal(false)
    }

    fun hangup() {
        hangupInternal(true)
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

                if (popBackStack) {
                    navManager.popBackStack()
                }
            }
        })
    }
}
