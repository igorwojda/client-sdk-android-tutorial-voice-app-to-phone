package com.vonage.tutorial.voice.extension

import androidx.fragment.app.FragmentManager

val FragmentManager.currentNavigationFragment
    get() = primaryNavigationFragment?.childFragmentManager?.fragments?.first()
