package cgy.memoriz

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.EditText

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
    })
}

fun EditText.validate(rule: Array<(String) -> Boolean>, errorMessage: Array<String>, sharedPref: SharedPref, num : Int) {
    this.afterTextChanged {
        loop@ for (i in rule.indices) {
            if (!rule[i](it)) {
                this.error = errorMessage[i]
                recordErrorCount(num, false, sharedPref)
                Log.d("error record", num.toString()+"false")
                break@loop
            } else {
                this.error = null
                recordErrorCount(num, true, sharedPref)
                Log.d("error record", num.toString()+"true")
            }
        }
    }
    loop@ for (i in rule.indices) {
        if (!rule[i](this.text.toString())) {
            this.error = errorMessage[i]
            recordErrorCount(num, false, sharedPref)
            Log.d("error record", num.toString()+"false")
            break@loop
        } else {
            this.error = null
            recordErrorCount(num, true, sharedPref)
            Log.d("error record", num.toString()+"true")
        }
    }
}

fun String.isEmailValid(): Boolean
        = this.isNotEmpty() &&
        Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isLengthAtLeast(num : Int): Boolean
        = this.isNotEmpty() &&
        this.length >= num

fun String.isOnlyLetterOrDigit() : Boolean {
    var checker = true

    if (this.isNotEmpty()) {
        for (i in this.indices) {
            if (!this[i].isLetterOrDigit() && this[i] != ' ') checker = false
        }
    }
    return checker
}

fun String.isPassMix(): Boolean {
    val checker : Array<Boolean> = arrayOf(false, false ,false)

    if (this.isNotEmpty()) {
        for (i in this.indices) {
            if (this[i].isDigit() && !checker[0]) checker[0] = true
            else if (this[i].isLetter() && this[i].isUpperCase() && !checker[1]) checker[1] = true
            else if (this[i].isLetter() && this[i].isLowerCase() && !checker[2]) checker[2] = true
        }
    }
    return checker[0] && checker[1] && checker[2]
}

fun recordErrorCount(num : Int, bool : Boolean, sharedPref: SharedPref) {
    if (num == 1) {
        sharedPref.registerError1 = bool
    }
    else if (num == 2) {
        sharedPref.registerError2 = bool
    }
    else if (num == 3) {
        sharedPref.registerError3 = bool
    }
}

fun checkErrorExist(sharedPref: SharedPref) : Boolean {
    return (sharedPref.registerError1 && sharedPref.registerError2 && sharedPref.registerError3)
}