package com.vonage.tutorial.voice

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.vonage.tutorial.R
import com.vonage.tutorial.voice.extension.observe
import com.vonage.tutorial.voice.extension.setText
import kotlinx.android.synthetic.main.fragment_voice.*

class VoiceFragment : Fragment(R.layout.fragment_voice), BackPressHandler {

    private val viewModel by viewModels<VoiceViewModel>()

    private var stateObserver = Observer<ViewState> {
        resetViewState()

        when(it) {
            is ViewState.Idle -> {
                logoutButton.isVisible = true
                logoutButton.setText(R.string.logout, it.currentUserName)

                callOtherUserButton.isVisible = true
                callOtherUserButton.setText(R.string.call, it.otherUserName)
            }
            is ViewState.Calling -> {
                messageTextView.isVisible = true
                messageTextView.setText(R.string.calling, it.otherUserName)
            }
            is ViewState.Call -> {
                hangupCallButton.isVisible = true
                hangupCallButton.setText(R.string.hangup_call_with, it.otherUserName)
            }
            is ViewState.IncomingCall -> {
                answerCallButton.isVisible = true
                answerCallButton.setText(R.string.answer_call_from, it.userName)
            }
            is Error -> {
                logoutButton.isVisible = true
                errorTextView.isVisible = true
                errorTextView.text = it.message
            }
        }
    }

    private fun resetViewState() {
        // TODO: create view extension - hide all children
        logoutButton.isVisible = true

        callOtherUserButton.isVisible = false
        errorTextView.isVisible = false
        messageTextView.isVisible = false
        answerCallButton.isVisible = false
        hangupCallButton.isVisible = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onInit()

        observe(viewModel.state, stateObserver)

        logoutButton.setOnClickListener {
            viewModel.onLogout()
            findNavController().popBackStack()
        }

        callOtherUserButton.setOnClickListener {
            viewModel.callOtherUser()
        }

        answerCallButton.setOnClickListener {
            viewModel.answerCall()
        }

        hangupCallButton.setOnClickListener {
            viewModel.hangup()
        }
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }
}
