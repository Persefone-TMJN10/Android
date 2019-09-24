package se.ju.lejo.persefone.Dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.app.Fragment

class CustomDialog(_title: String, _message: String, currentActivity: Context?) {

    private val builder = AlertDialog.Builder(currentActivity)
    private var dialog: AlertDialog? = null
    private var activityContext: Context? = currentActivity

    init {
        builder.setTitle(_title)
        builder.setMessage(_message)
        builder.setPositiveButton("Got it!") { dialog, which ->
            //if we want to add something after user pressed "Got it!"
        }
        dialog = builder.create()
    }

    fun show() {
        vibratePhone(activityContext)
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }

    fun vibratePhone(currentActivity: Context?) {
        val vibrator = currentActivity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }
}