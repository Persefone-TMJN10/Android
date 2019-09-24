package se.ju.lejo.persefone.Time

import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.widget.TextView
import se.ju.lejo.persefone.Dialog.CustomDialog
import java.util.*
import java.util.concurrent.TimeUnit

class Timer(val _timerTextView: TextView, currentActivity: Context?) {

    val _countDownInterval: Long = 1000
    val _currentActivity: Context? = currentActivity
    var _dialog: CustomDialog? = null

    // Method to configure and return an instance of CountDownTimer object
    fun timerCountDown(millisInFuture:Long): CountDownTimer {

        return object: CountDownTimer(millisInFuture,_countDownInterval) {

            override fun onTick(millisUntilFinished: Long) {
                val timeRemaining = timeString(millisUntilFinished)

                //_radiationUnitsUsed += DataHandler.getE()

                _timerTextView.text = timeRemaining

                // edit here intervals for vibration
                if (millisUntilFinished <= 60000 && getSeconds(millisUntilFinished) % 10 == 0) {
                    _dialog?.dismiss()
                    _dialog = CustomDialog("Warning", "You only have " + getSeconds(millisUntilFinished) + " seconds left before you must clock out and leave the facility!", _currentActivity)
                    _dialog?.show()
                }
            }

            override fun onFinish() {
                val timeRemaining = timeString(0)

                _timerTextView.setTextColor(Color.RED)
                _timerTextView.text = timeRemaining
            }
        }
    }

    fun getSeconds(millisUntilFinished: Long): Int {
        return (millisUntilFinished.toInt() / 1000)
    }

    // Method to get days hours minutes seconds from milliseconds
    fun timeString(millisUntilFinished:Long): String {
        var millisUntilFinished:Long = millisUntilFinished
        val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
        millisUntilFinished -= TimeUnit.DAYS.toMillis(days)

        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
        millisUntilFinished -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
        millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)

        // Format the string
        return String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            hours, minutes,seconds
        )
    }
}