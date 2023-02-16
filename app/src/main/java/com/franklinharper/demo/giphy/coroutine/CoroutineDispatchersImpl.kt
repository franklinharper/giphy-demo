@file:Suppress("unused")

package com.franklinharper.demo.giphy.coroutine

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

// Use real dependencies when running the app.
// Test dispatchers are used when testing.
class CoroutineDispatchersImpl @Inject constructor(): CoroutineDispatchers {
    override val io = Dispatchers.IO
}
