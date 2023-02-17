package com.franklinharper.demo.giphy

import android.app.Application
import android.util.Log
import androidx.viewbinding.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    /** A tree which logs important information for crash reporting.  */
    private class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
            // TODO implement remote crash reporting in production
            // Example code below is from Timber github.
            //            FakeCrashLibrary.log(priority, tag, message)
            //            if (t != null) {
            //                if (priority == Log.ERROR) {
            //                    FakeCrashLibrary.logError(t)
            //                } else if (priority == Log.WARN) {
            //                    FakeCrashLibrary.logWarning(t)
            //                }
            //            }
        }
    }

}