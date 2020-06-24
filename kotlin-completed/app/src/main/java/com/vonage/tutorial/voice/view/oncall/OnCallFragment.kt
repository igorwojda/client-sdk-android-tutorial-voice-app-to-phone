package com.vonage.tutorial.voice.view.oncall

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
import kotlinx.android.synthetic.main.fragment_on_call.*

class OnCallFragment : Fragment(R.layout.fragment_on_call),
    BackPressHandler {

    private val viewModel by viewModels<OnCallViewModel>()

    private val args by navArgs<OnCallFragmentArgs>()

    private val toastObserver = Observer<String> {
        context?.toast(it)
    }

    private val otherUserNameObserver = Observer<String> {
        otherUserNameTextView.text = it
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onInit(args)

        observe(viewModel.toastLiveData, toastObserver)
        observe(viewModel.otherUserNameLiveData, otherUserNameObserver)

        hangupFab.setOnClickListener {
            viewModel.hangup()
        }
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }
}
