package com.vonage.tutorial.voice

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.vonage.tutorial.R
import com.vonage.tutorial.voice.extension.observe
import com.vonage.tutorial.voice.extension.toast
import kotlinx.android.synthetic.main.fragment_incoming_call.*
import kotlinx.android.synthetic.main.fragment_on_call.*
import kotlinx.android.synthetic.main.fragment_on_call.hangupFab

class IncomingCallFragment : Fragment(R.layout.fragment_incoming_call), BackPressHandler {

    private val toastObserver = Observer<String> {
        context?.toast(it)
    }

    private val viewModel by viewModels<IncomingViewModel>()
    private val args by navArgs<IncomingCallFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observe(viewModel.toastLiveData, toastObserver)

        // Todo: take from call
        otherUserNameTextView.text = args.otherUserName

        hangupFab.setOnClickListener {
            viewModel.hangup()
        }

        answerFab.setOnClickListener {
            viewModel.answer()
        }
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }
}
