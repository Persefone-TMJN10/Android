package se.ju.lejo.persefone.Time

import android.graphics.Color
import android.os.CountDownTimer
import android.widget.TextView
import se.ju.lejo.persefone.Dialog.CustomDialog
import java.util.*
import java.util.concurrent.TimeUnit

class Timer(val _timerTextView: TextView) {

    val _countDownInterval: Long = 1000
    val _dialogObject: CustomDialog? = null

    // Method to configure and return an instance of CountDownTimer object
    fun timerCountDown(millisInFuture:Long): CountDownTimer {

        return object: CountDownTimer(millisInFuture,_countDownInterval) {

            override fun onTick(millisUntilFinished: Long) {
                val timeRemaining = timeString(millisUntilFinished)

                _timerTextView.text = timeRemaining
                if (getSeconds(millisUntilFinished) % 10 == 0) {
                    println("Even for 10")
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