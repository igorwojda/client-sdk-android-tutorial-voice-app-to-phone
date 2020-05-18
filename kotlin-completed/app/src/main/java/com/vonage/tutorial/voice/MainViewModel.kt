package com.vonage.tutorial.voice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nexmo.client.NexmoCall
import com.nexmo.client.NexmoCallHandler
import com.nexmo.client.NexmoClient
import com.nexmo.client.NexmoIncomingCallListener
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

    private lateinit var otherUser: User

    private val toastMutableLiveData = MutableLiveData<String>()
    val toastLiveData = toastMutableLiveData.asLiveData()

    private val loadingMutableLiveData = MutableLiveData<Boolean>()
    val loadingLiveData = loadingMutableLiveData.asLiveData()

    private val incomingCallListener = NexmoIncomingCallListener { call ->
        callManager.onGoingCall = call
        val otherUserName = call.callMembers.first().user.name
        val navDirections = MainFragmentDirections.actionMainFragmentToIncomingCallFragment(otherUserName)
        navManager.navigate(navDirections)
    }

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

    private val _state = MutableLiveData<String>()
    val state = _state.asLiveData()

    fun onInit(currentUserName: String) {
        otherUser = Config.getOtherUser(currentUserName)

        client.addIncomingCallListener(incomingCallListener)
    }

    override fun onCleared() {
        client.removeIncomingCallListeners()
    }

    fun startInAppCall() {
        startCall(NexmoCallHandler.IN_APP)
    }

    fun startPhoneCall() {
        startCall(NexmoCallHandler.SERVER)
    }

    private fun startCall(callType: NexmoCallHandler) {
        client.call(otherUser.name, callType, callListener)
        loadingMutableLiveData.postValue(true)
    }

    fun onBackPressed() {
        client.logout()
    }
}

