package com.keithsmyth.androidshowcase.domain

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Strings @Inject constructor(
    @ApplicationContext private val appContext: Context,
) {

    fun get(@StringRes stringRes: Int): String {
        return appContext.getString(stringRes)
    }

    fun get(@StringRes stringRes: Int, vararg formatArgs: Any?): String {
        return appContext.getString(stringRes, *formatArgs)
    }
}
