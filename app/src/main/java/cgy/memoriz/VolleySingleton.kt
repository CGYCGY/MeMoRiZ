package cgy.memoriz

import android.app.Application
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

// singleton class used for process volley request queue
class VolleySingleton: Application() {
    override fun onCreate() {
        super.onCreate()
        instance =this
    }

    private var _requestQueue: RequestQueue? = null
    val requestQueue: RequestQueue?
        get() {
            if (_requestQueue == null) {
                _requestQueue = Volley.newRequestQueue(applicationContext)
            }
            return _requestQueue
        }

//    val requestQueue: RequestQueue? = null
//        get() {
//            if(field == null) {
//                return Volley.newRequestQueue(applicationContext)
//            }
//            return field
//        }

    fun <T> addToRequestQueue(request: Request<T>, tag: String) {
        request.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        requestQueue?.add(request)
    }

    fun <T> addToRequestQueue(request: Request<T>) {
        request.tag = TAG
        requestQueue?.add(request)
    }

    fun cancelPendingRequests(tag: Any) {
        if (requestQueue != null) {
            requestQueue!!.cancelAll(tag)
        }
    }

    companion object {
        private val TAG = VolleySingleton::class.java.simpleName
        @get:Synchronized var instance: VolleySingleton? = null
            private set
    }
}