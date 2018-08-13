package cgy.memoriz

import android.content.Context
import android.content.SharedPreferences

object sharedPref {
    private lateinit var sharedPreferences: SharedPreferences

    // list of app specific sharedPreferences
    private val registerErrorFirstRun1 = Pair("register error 1", false)
    private val registerErrorFirstRun2 = Pair("register error 2", false)
    private val registerErrorFirstRun3 = Pair("register error 3", false)

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("memoriz", Context.MODE_PRIVATE)
    }

//    set this so that no nid to call edit() and apply() in here
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var registerError1: Boolean
//        get( name , data )
        get() = sharedPreferences.getBoolean(registerErrorFirstRun1.first, registerErrorFirstRun1.second)

//        set ( name , data )
        set(value) = sharedPreferences.edit {
            it.putBoolean(registerErrorFirstRun1.first, value)
        }

    var registerError2: Boolean
        get() = sharedPreferences.getBoolean(registerErrorFirstRun2.first, registerErrorFirstRun2.second)

        set(value) = sharedPreferences.edit {
            it.putBoolean(registerErrorFirstRun2.first, value)
        }

    var registerError3: Boolean
        get() = sharedPreferences.getBoolean(registerErrorFirstRun3.first, registerErrorFirstRun3.second)

        set(value) = sharedPreferences.edit {
            it.putBoolean(registerErrorFirstRun3.first, value)
        }
}