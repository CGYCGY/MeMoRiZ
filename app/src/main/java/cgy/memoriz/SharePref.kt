package cgy.memoriz

import android.content.Context
import android.content.SharedPreferences

object SharedPref {
    private lateinit var sharedPreferences: SharedPreferences

    // list of app specific sharedPreferences
    private val registerErrorData1 = Pair("register error 1", false)
    private val registerErrorData2 = Pair("register error 2", false)
    private val registerErrorData3 = Pair("register error 3", false)
    private val uiStateData = Pair("ui state", false)
    private val arrowData = Pair("arrow", false)
    private val userNameData = Pair("user's name", "")
    private val userEmailData = Pair("user's email", "")

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
        get() = sharedPreferences.getBoolean(registerErrorData1.first, registerErrorData1.second)

//        set ( name , data )
        set(value) = sharedPreferences.edit {
            it.putBoolean(registerErrorData1.first, value)
        }

    var registerError2: Boolean
        get() = sharedPreferences.getBoolean(registerErrorData2.first, registerErrorData2.second)

        set(value) = sharedPreferences.edit {
            it.putBoolean(registerErrorData2.first, value)
        }

    var registerError3: Boolean
        get() = sharedPreferences.getBoolean(registerErrorData3.first, registerErrorData3.second)

        set(value) = sharedPreferences.edit {
            it.putBoolean(registerErrorData3.first, value)
        }

    var uiState: Boolean
        get() = sharedPreferences.getBoolean(uiStateData.first, uiStateData.second)

        set(value) = sharedPreferences.edit {
            it.putBoolean(uiStateData.first, value)
        }
    var arrow: Boolean
        get() = sharedPreferences.getBoolean(arrowData.first, arrowData.second)

        set(value) = sharedPreferences.edit {
            it.putBoolean(arrowData.first, value)
        }

    var userName: String
        get() = sharedPreferences.getString(userNameData.first, userNameData.second)

        set(value) = sharedPreferences.edit {
            it.putString(userNameData.first, value)
        }

    var userEmail: String
        get() = sharedPreferences.getString(userEmailData.first, userEmailData.second)

        set(value) = sharedPreferences.edit {
            it.putString(userEmailData.first, value)
        }
}