package com.vonage.tutorial.voice.view.incommingcall

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.vonage.tutorial.R
import com.vonage.tutorial.voice.BackPressHandler
import com.vonage.tutorial.voice.extension.observe
import com.vonage.tutorial.voice.extension.toast
import kotlinx.android.synthetic.main.fragment_incoming_call.*

class IncomingCallFragment : Fragment(R.layout.fragment_incoming_call),
    BackPressHandler {

    private val toastObserver = Observer<String> {
        context?.toast(it)
    }

    private val otherUserNameLiveData = Observer<String> {
        userNameTextView.text = it
    }

    private val viewModel by viewModels<IncomingViewModel>()
    private val args by navArgs<IncomingCallFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onInit(args)

        observe(viewModel.toastLiveData, toastObserver)
        observe(viewModel.otherUserNameLiveData, otherUserNameLiveData)

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
