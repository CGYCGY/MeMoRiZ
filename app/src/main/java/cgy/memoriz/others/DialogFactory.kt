package cgy.memoriz.others

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import cgy.memoriz.R

class DialogFactory {

    fun createTwoButtonDialog(context: Context, title: String, message: String, clickListener: DialogInterface.OnClickListener): Dialog {
        val alertDialog = AlertDialog.Builder(context, R.style.AlertDialogTheme)
        alertDialog.setTitle("$title")
                .setMessage("$message")
                .setPositiveButton("YES", clickListener)
                .setNegativeButton("NO", DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
        return alertDialog.create()
    }

    fun createOneButtonDialog(context: Context, title: String, message: String, buttonName: String): Dialog {
        val alertDialog = AlertDialog.Builder(context, R.style.AlertDialogTheme)
        alertDialog.setTitle("$title")
                .setMessage("$message")
                .setPositiveButton(buttonName, DialogInterface.OnClickListener{ dialog, which ->
                    dialog.dismiss()
                })
        return alertDialog.create()
    }
}