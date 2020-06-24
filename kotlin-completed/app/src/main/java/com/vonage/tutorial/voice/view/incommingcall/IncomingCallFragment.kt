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
import com.vonage.tutorial.voice.extension.setText
import com.vonage.tutorial.voice.extension.toast
import kotlinx.android.synthetic.main.fragment_incoming_call.*

class IncomingCallFragment : Fragment(R.layout.fragment_incoming_call),
    BackPressHandler {

    private val toastObserver = Observer<String> {
        context?.toast(it)
    }

    private val viewModel by viewModels<IncomingViewModel>()
    private val args by navArgs<IncomingCallFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.toastLiveData, toastObserver)

        incomingCallTextView.setText(R.string.incoming_call_from, args.otherUserName)


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
