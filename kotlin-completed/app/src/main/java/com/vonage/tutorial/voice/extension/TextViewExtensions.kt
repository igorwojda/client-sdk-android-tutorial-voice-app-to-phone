package com.vonage.tutorial.voice.extension

import android.widget.TextView
import androidx.annotation.StringRes

fun TextView.setText(@StringRes res: Int, vararg formatArgs: Any?) {
    text = resources.getString(res, *formatArgs)
}