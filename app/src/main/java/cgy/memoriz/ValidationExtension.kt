package cgy.memoriz

import android.text.Editable
import android.text.TextWatcher
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

fun EditText.validate(rule: Array<(String) -> Boolean>, errorMessage: Array<String>) {
    this.afterTextChanged {
        loop@ for (i in rule.indices) {
            if (!rule[i](it)) {
                this.error = errorMessage[i]
                break@loop
            } else this.error = null
        }
    }
    loop@ for (i in rule.indices) {
        if (!rule[i](this.text.toString())) {
            this.error = errorMessage[i]
            break@loop
        } else this.error = null
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
            if (!this[i].isLetterOrDigit()) checker = false
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