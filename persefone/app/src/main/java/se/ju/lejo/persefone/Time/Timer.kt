package se.ju.lejo.persefone.Time

import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.widget.TextView
import se.ju.lejo.persefone.Data.DataHandler
import se.ju.lejo.persefone.Dialog.CustomDialog
import java.util.*
import java.util.concurrent.TimeUnit

class Timer(val timerTextView: TextView, currentActivity: Context?) {

    val countDownInterval: Long = 1000
    val currentActivity: Context? = currentActivity
    var dialog: CustomDialog? = null
    var cDTimer: CountDownTimer? = null
    
    fun startTimer(millisInFuture: Long) {

        cDTimer = object: CountDownTimer(millisInFuture,countDownInterval) {

            override fun onTick(millisUntilFinished: Long) {
                val timeRemaining = timeString(millisUntilFinished)

                DataHandler.incrementET()
                timerTextView.setTextColor(Color.BLACK)
                timerTextView.text = timeRemaining

                if (millisUntilFinished <= 60000 && getSeconds(millisUntilFinished) % 10 == 0 && getSeconds(millisUntilFinished) != 0) {
                    sendDialogMessage("ATTENTION", "You only have " + getSeconds(millisUntilFinished) + " seconds left before you must clock out and leave the facility")
                }
            }

            override fun onFinish() {
                val timeRemaining = timeString(0)
                timerTextView.setTextColor(Color.RED)
                timerTextView.text = timeRemaining
                sendDialogMessage("WARNING", "You have overstayed your safety time, you must clock out and leave the facility immediately")
            }
        }

        cDTimer?.start()
    }

    fun stopTimer() {
        cDTimer?.cancel()
        cDTimer = null

        val timeRemaining = timeString(0)
        timerTextView.text = timeRemaining
    }

    fun sendDialogMessage(title: String, message: String) {
        dialog?.dismiss()
        dialog = CustomDialog(title, message, currentActivity)
        dialog?.show()
    }

    fun getSeconds(millisUntilFinished: Long): Int {
        return (millisUntilFinished.toInt() / 1000)
    }

    fun timeString(millisUntilFinished:Long): String {
        var millisUntilFinished:Long = millisUntilFinished
        val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
        millisUntilFinished -= TimeUnit.DAYS.toMillis(days)

        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
        millisUntilFinished -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
        millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)

        return String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d:%02d",
            days, hours, minutes,seconds
        )
    }
}