package com.vonage.tutorial.voice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.vonage.tutorial.R
import com.vonage.tutorial.voice.extension.currentNavigationFragment
import com.vonage.tutorial.voice.util.NavManager
import kotlinx.android.synthetic.main.activity_nav_host.*

class NavHostActivity : AppCompatActivity(R.layout.activity_nav_host) {

    private val navManager = NavManager
    private val navController get() = navHostFragment.findNavController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navManager.init(navController)
    }

    override fun onBackPressed() {
        val backPressHandler = supportFragmentManager.currentNavigationFragment as? BackPressHandler
        backPressHandler?.onBackPressed()

        super.onBackPressed()
    }
}
