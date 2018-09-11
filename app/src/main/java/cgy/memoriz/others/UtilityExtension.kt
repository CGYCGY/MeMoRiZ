package cgy.memoriz.others

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.util.*
import kotlin.collections.ArrayList

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun ArrayList<String>.shuffleString(): ArrayList<String> {
    val rng = Random()

    for (index in 0 until this.size) {
        val randomIndex = rng.nextInt(this.size)

        // Swap with the random position
        val temp = this[index]
        this[index] = this[randomIndex]
        this[randomIndex] = temp
    }

    return this
}

fun ArrayList<String>.filterString(s : String) : ArrayList<String> {
    val same = ArrayList<Int>()

    for (i in 0 until this.size) {
        if (this[i] == s) same.add(i)
    }

    same.reverse()

    for (i in 0 until same.size) {
        this.removeAt(same[i])
    }

    return this
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}