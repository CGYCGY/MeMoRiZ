package cgy.memoriz.others

import android.app.Dialog
import android.content.Context
import dmax.dialog.SpotsDialog

open class SDialog {
    private var spDialog : Dialog

    constructor(message : String, context: Context) {
        spDialog = SpotsDialog.Builder()
                .setContext(context)
                .setMessage(message)
                .build()
    }

    fun showDialog() {
        if (!spDialog.isShowing)
            spDialog.show()
    }

    fun hideDialog() {
        if (spDialog.isShowing)
            spDialog.dismiss()
    }
}