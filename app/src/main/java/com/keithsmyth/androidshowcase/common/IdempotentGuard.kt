package com.keithsmyth.androidshowcase.common

import androidx.annotation.MainThread

/**
 * Avoid launching asynchronous side effects to class creation. Use this guard instead.
 * https://developer.android.com/topic/architecture/ui-layer/state-production#initializing-state-production
 */
class IdempotentGuard(private val block: () -> Unit) {
    private var hasRun = false

    @MainThread
    fun run() {
        if (hasRun) return
        hasRun = true
        block()
    }

    fun force() {
        block()
    }
}
