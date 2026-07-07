package com.remindrx

import android.app.Application
import com.remindrx.reminders.Notifications

class RemindRxApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Notifications.ensureChannel(this)
    }
}

