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

//fun EditText.validate(rule1: Array<(String) -> Boolean>, errorMessage1: Array<String>) {
//    this.afterTextChanged {
//        loop@ for (i in rule1.indices) {
//            this.error = if (rule1[i](it)) null else {
//                errorMessage1[i]
//                break@loop
//            }
//        }
//    }
//    loop@ for (i in rule1.indices) {
//        this.error = if (rule1[i](this.text.toString())) null else {
//            errorMessage1[i]
//            break@loop
//        }
//    }
//}

fun EditText.validate(rule1: Array<(String) -> Boolean>, errorMessage1: Array<String>) {
    this.afterTextChanged {
        loop@ for (i in rule1.indices) {
            if (!rule1[i](it)) {
                this.error = errorMessage1[i]
                break@loop
            } else this.error = null
        }
    }
    loop@ for (i in rule1.indices) {
        if (!rule1[i](this.text.toString())) {
            this.error = errorMessage1[i]
            break@loop
        } else this.error = null
    }
}

//fun EditText.validate(rule1: (String) -> Boolean, errorMessage1: String) {
//    this.afterTextChanged {
//        this.error = if (rule1(it)) null else errorMessage1
//    }
//    this.error = if (rule1(this.text.toString())) null else errorMessage1
//}
//
//fun EditText.validate(rule1: (String) -> Boolean, rule2: (String) -> Boolean, errorMessage1: String, errorMessage2: String) {
//    this.afterTextChanged {
//        this.error = if (!rule1(it)) errorMessage1 else if (!rule2(it)) errorMessage2 else null
//    }
//    this.error = if (!rule1(this.text.toString())) errorMessage1 else if (!rule2(this.text.toString())) errorMessage2 else null
//}

fun String.isEmailValid(): Boolean
        = this.isNotEmpty() &&
        Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isPassFormatCorrect(): Boolean
        = this.isNotEmpty() &&
        this.length >= 6