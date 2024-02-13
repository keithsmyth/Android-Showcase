package com.keithsmyth.androidshowcase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Dispatchers @Inject constructor() {
    fun io(): CoroutineDispatcher = Dispatchers.IO
}
