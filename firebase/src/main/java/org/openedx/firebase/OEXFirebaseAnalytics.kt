package org.openedx.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import org.openedx.foundation.interfaces.Analytics

class OEXFirebaseAnalytics(context: Context) : Analytics {
    private var tracker: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    init {
        Log.d(TAG, "Firebase Analytics Builder Initialised")
    }

    override fun logScreenEvent(screenName: String, params: Map<String, Any?>) {
        tracker.logEvent(screenName, params.toBundle())
        Log.d(TAG, "Firebase Analytics log Screen Event: $screenName + $params")
    }

    override fun logEvent(eventName: String, params: Map<String, Any?>) {
        tracker.logEvent(eventName, params.toBundle())
        Log.d(TAG, "Firebase Analytics log Event $eventName: $params")
    }

    override fun logUserId(userId: Long) {
        tracker.setUserId(userId.toString())
        Log.d(TAG, "Firebase Analytics User Id log Event")
    }

    private companion object {
        const val TAG = "FirebaseAnalytics"
    }
}
