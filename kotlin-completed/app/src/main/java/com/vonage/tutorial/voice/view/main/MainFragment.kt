package com.vonage.tutorial.voice.view.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.vonage.tutorial.R
import com.vonage.tutorial.voice.BackPressHandler
import com.vonage.tutorial.voice.extension.observe
import com.vonage.tutorial.voice.extension.setText
import com.vonage.tutorial.voice.extension.toast
import kotlinx.android.synthetic.main.fragment_main.*
import kotlin.properties.Delegates

class MainFragment : Fragment(R.layout.fragment_main), BackPressHandler {

    private var dataLoading: Boolean by Delegates.observable(false) { _, _, newValue ->
        startAppToPhoneCallButton.isEnabled = !newValue
        progressBar.isVisible = newValue
    }

    private val args: MainFragmentArgs by navArgs()

    private val viewModel by viewModels<MainViewModel>()

    private val toastObserver = Observer<String> {
        context?.toast(it)
    }

    private val loadingObserver = Observer<Boolean> {
        dataLoading = it
    }

    private val currentUserObserver = Observer<String> {
        currentUserNameTextView.setText(R.string.hi, it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onInit(args)

        observe(viewModel.toastLiveData, toastObserver)
        observe(viewModel.loadingLiveData, loadingObserver)
        observe(viewModel.currentUserNameLiveData, currentUserObserver)

        startAppToPhoneCallButton.setOnClickListener {
            viewModel.startAppToPhoneCall()
        }
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }
}
