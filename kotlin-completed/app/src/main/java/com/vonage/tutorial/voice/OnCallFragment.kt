package com.vonage.tutorial.voice

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.vonage.tutorial.R
import com.vonage.tutorial.voice.extension.observe
import com.vonage.tutorial.voice.extension.toast
import kotlinx.android.synthetic.main.fragment_on_call.*

class OnCallFragment : Fragment(R.layout.fragment_on_call), BackPressHandler {

    private val viewModel by viewModels<OnCallViewModel>()

    private val toastObserver = Observer<String> {
        context?.toast(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.toastLiveData, toastObserver)

        hangupFab.setOnClickListener {
            viewModel.hangup()
        }
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }
}
