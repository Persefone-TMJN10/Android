package se.ju.lejo.persefone.Dialog

import android.app.AlertDialog
import android.content.Context

class CustomDialog(_title: String, _message: String, currentActivity: Context?) {

    private val builder = AlertDialog.Builder(currentActivity)
    private var dialog: AlertDialog? = null

    init {
        builder.setTitle(_title)
        builder.setMessage(_message)
        builder.setPositiveButton("Got it!") { dialog, which ->
            //if we want to add something after user pressed "Got it!"
        }
        dialog = builder.create()
    }

    fun show() {
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}