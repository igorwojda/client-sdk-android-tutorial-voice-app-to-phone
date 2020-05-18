package com.vonage.tutorial.voice

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.nexmo.client.request_listener.NexmoConnectionListener.ConnectionStatus
import com.vonage.tutorial.R
import com.vonage.tutorial.voice.extension.observe
import com.vonage.tutorial.voice.extension.toast
import kotlinx.android.synthetic.main.fragment_login.*
import kotlin.properties.Delegates

class LoginFragment : Fragment(R.layout.fragment_login) {

    companion object {
        private const val CALL_PERMISSIONS_REQUEST = 123

        private val callsPermissions = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECORD_AUDIO)
    }

    private val viewModel by viewModels<LoginViewModel>()

    private var dataLoading: Boolean by Delegates.observable(false) { _, _, newValue ->
        loginAsAliceButton.isEnabled = !newValue
        loginAsBobButton.isEnabled = !newValue
        progressBar.isVisible = newValue
    }

    private val stateObserver = Observer<ConnectionStatus> {
        connectionStatusTextView.text = it.toString()

        if (it == ConnectionStatus.DISCONNECTED) {
            dataLoading = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.connectionStatus, stateObserver)

        loginAsAliceButton.setOnClickListener {
            loginUser(Config.alice)
        }

        loginAsBobButton.setOnClickListener {
            loginUser(Config.bob)
        }

        requestCallPermissions()
    }

    private fun loginUser(user: User) {
        if (user.jwt.isBlank()) {
            activity?.toast("Error: Please set Config.${user.name.toLowerCase()}.jwt")
        } else {
            viewModel.onLoginUser(user)
            dataLoading = true
        }
    }

    private fun requestCallPermissions() {
        val activity = requireNotNull(activity)

        callsPermissions
            .filter { ActivityCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED }
            .onEach {
                ActivityCompat.requestPermissions(
                    activity,
                    callsPermissions,
                    CALL_PERMISSIONS_REQUEST
                )
            }
    }
}
