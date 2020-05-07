package com.vonage.tutorial.voice

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nexmo.client.NexmoCall
import com.nexmo.client.NexmoCallEventListener
import com.nexmo.client.NexmoCallHandler
import com.nexmo.client.NexmoCallMember
import com.nexmo.client.NexmoCallMemberStatus
import com.nexmo.client.NexmoClient
import com.nexmo.client.NexmoIncomingCallListener
import com.nexmo.client.NexmoMediaActionState
import com.nexmo.client.request_listener.NexmoApiError
import com.nexmo.client.request_listener.NexmoRequestListener
import com.vonage.tutorial.voice.extension.asLiveData
import kotlin.properties.Delegates

class VoiceViewModel : ViewModel() {

    private val client = NexmoClient.get()
    private var call by Delegates.observable<NexmoCall?>(null) { _, oldValue, newValue ->
        oldValue?.removeCallEventListener(callEventListener)
        newValue?.addCallEventListener(callEventListener)
    }

    private val _state = MutableLiveData<ViewState>()
    val state = _state.asLiveData()

    private val currentUserName = client.user.name
    private val otherUserName = Config.getOtherUserName(currentUserName)

    private val callEventListener = object : NexmoCallEventListener {
        override fun onDTMF(p0: String?, callMember: NexmoCallMember?) {
            Log.d("AAA", "onDTMF")
        }

        override fun onMemberStatusUpdated(callMemberStatus: NexmoCallMemberStatus?, callMember: NexmoCallMember?) {
            Log.d("AAA", "onMemberStatusUpdated ${callMember?.user?.name} : $callMemberStatus")

            if (callMemberStatus == NexmoCallMemberStatus.COMPLETED || callMemberStatus == NexmoCallMemberStatus.CANCELED) {
                postIdleState()
            }

        }

        override fun onMuteChanged(mediaActionState: NexmoMediaActionState?, callMember: NexmoCallMember?) {
            Log.d("AAA", "onMuteChanged")
        }

        override fun onEarmuffChanged(mediaActionState: NexmoMediaActionState?, callMember: NexmoCallMember?) {
            Log.d("AAA", "onEarmuffChanged")
        }
    }

    private val callUserListener = object : NexmoRequestListener<NexmoCall> {
        override fun onSuccess(call: NexmoCall?) {
            this@VoiceViewModel.call = call
        }

        override fun onError(apiError: NexmoApiError) {
            _state.postValue(ViewState.Error(apiError.message))
        }
    }

    private val answerCallListener = object : NexmoRequestListener<NexmoCall> {
        override fun onSuccess(call: NexmoCall?) {
            this@VoiceViewModel.call = call
            val otherUserName = call?.callMembers?.first()?.user?.name ?: ""
            _state.postValue(ViewState.Call(otherUserName))
        }

        override fun onError(apiError: NexmoApiError) {
            _state.postValue(ViewState.Error(apiError.message))
        }
    }

    private val hangupCallListener = object : NexmoRequestListener<NexmoCall> {
        override fun onSuccess(call: NexmoCall?) {
            this@VoiceViewModel.call = call

            postIdleState()
        }

        override fun onError(apiError: NexmoApiError) {
            _state.postValue(ViewState.Error(apiError.message))
        }
    }

    private val incomingCallListener = NexmoIncomingCallListener { call ->
        this@VoiceViewModel.call = call
        // call.callMembers means call.otherCallMembers
        _state.postValue(ViewState.IncomingCall(call.callMembers.first().user.name))
    }

    fun onInit() {
        postIdleState()
        client.addIncomingCallListener(incomingCallListener)
    }

    private fun postIdleState() {
        val viewState = ViewState.Idle(currentUserName, otherUserName)
        _state.postValue(viewState)
    }

    fun onBackPressed() {
        client.logout()
    }

    fun onLogout() {
        client.logout()
    }

    override fun onCleared() {
        client.removeIncomingCallListeners()
        call?.removeCallEventListener(callEventListener)
    }

    fun callOtherUser() {
        client.call(otherUserName, NexmoCallHandler.IN_APP, callUserListener)
        _state.postValue(ViewState.Calling(otherUserName))
    }

    fun answerCall() {
        call?.answer(answerCallListener)
    }

    fun hangup() {
        call?.hangup(hangupCallListener)
    }
}

sealed class ViewState {
    data class Idle(val currentUserName: String, val otherUserName: String) : ViewState()
    data class Calling(val otherUserName: String) : ViewState()
    data class IncomingCall(val userName: String) : ViewState()
    data class  Call(val otherUserName: String) : ViewState()
    data class Error(val message: String) : ViewState()
}
