package com.vonage.tutorial.voice.view.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nexmo.client.NexmoClient
import com.nexmo.client.request_listener.NexmoConnectionListener.ConnectionStatus
import com.vonage.tutorial.voice.User
import com.vonage.tutorial.voice.extension.asLiveData
import com.vonage.tutorial.voice.util.NavManager

class LoginViewModel : ViewModel() {

    private val navManager = NavManager

    private val _connectionStatus = MutableLiveData<ConnectionStatus>()
    val connectionStatus = _connectionStatus.asLiveData()

    private var user: User? = null

    private val client: NexmoClient = TODO("Retrieve NexmoClient instance")

    init {
        TODO("Init connection listener")
    }

    private fun navigateToMainFragment() {
        val userName = checkNotNull(user?.name) { "user is null" }
        val navDirections =
            LoginFragmentDirections.actionLoginFragmentToVoiceFragment(
                userName
            )
        navManager.navigate(navDirections)
    }

    fun onLoginUser(user: User) {
        // TODO: Login user
    }
}
