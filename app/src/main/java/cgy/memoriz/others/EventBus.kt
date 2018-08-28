package cgy.memoriz.others

import android.util.Log
import com.squareup.otto.Bus

open class EventBus {
    private val LOG_TAG = "EventBus"

    companion object {
        var eventBus: Bus? = null
    }

    fun initBus() {
        try {
            if (eventBus != null) {
                if (Log.isLoggable(LOG_TAG, Log.WARN)) {
                    Log.w(LOG_TAG, "Warning! Event Bus init more than once.")
                }
            }
            eventBus = Bus("eventBus")
        } catch (e: Exception) {
            if (Log.isLoggable(LOG_TAG, Log.ERROR)) {
                Log.e(LOG_TAG, "initBus", e)
            }
        }

    }

    fun getDefaultBus(): Bus? {
        return eventBus
    }

    fun post(event: Any) {
        if (eventBus != null) {
            try {
                eventBus!!.post(event)
            } catch (e: Exception) {
                if (Log.isLoggable(LOG_TAG, Log.ERROR)) {
                    Log.e(LOG_TAG, "post, event=[$event]", e)
                }
            }

        }
    }

    fun registerOnBus(`object`: Any?) {
        try {
            if (eventBus != null && `object` != null) {
                eventBus!!.register(`object`)
            }
        } catch (e: Exception) {
            if (Log.isLoggable(LOG_TAG, Log.ERROR)) {
                Log.e(LOG_TAG, "registerOnBus, object=[$`object`]", e)
            }
        }

    }

    fun unregisterFromBus(`object`: Any) {
        try {
            if (eventBus != null) {
                eventBus!!.unregister(`object`)
            }
        } catch (e: Exception) {
            if (Log.isLoggable(LOG_TAG, Log.ERROR)) {
                Log.e(LOG_TAG, "unregisterFromBus, context=[$`object`]", e)
            }
        }

    }
}