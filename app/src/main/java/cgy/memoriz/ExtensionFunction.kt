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

//validate for only 1 rule
fun EditText.validate(rule: (String) -> Boolean, errorMessage: String) {
    this.afterTextChanged {
        this.error = if (!rule(it)) errorMessage else null
    }
//    this.error = if (!rule(this.text.toString())) errorMessage else null
}

//validate for more than 1 rule
fun EditText.validate(rule: Array<(String) -> Boolean>, errorMessage: Array<String>) {
    this.afterTextChanged {
        loop@ for (i in rule.indices) {
            if (!rule[i](it)) {
                this.error = errorMessage[i]
                break@loop
            } else this.error = null
        }
    }
//    loop@ for (i in rule.indices) {
//        if (!rule[i](this.text.toString())) {
//            this.error = errorMessage[i]
//            break@loop
//        } else this.error = null
//    }
}

fun String.isEmailValid(): Boolean
        = this.isNotEmpty() &&
        Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isPassFormatCorrect(): Boolean
        = this.isNotEmpty() &&
        this.length >= 6