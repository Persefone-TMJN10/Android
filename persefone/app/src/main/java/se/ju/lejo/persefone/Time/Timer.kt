package se.ju.lejo.persefone.Timer

import android.os.CountDownTimer
import android.widget.TextView
import java.util.*
import java.util.concurrent.TimeUnit

class Timer(val timerTextView: TextView) {

    val countDownInterval:Long = 1000

    // Method to configure and return an instance of CountDownTimer object
    fun timer(millisInFuture:Long): CountDownTimer {

        return object: CountDownTimer(millisInFuture,countDownInterval){

            override fun onTick(millisUntilFinished: Long){
                val timeRemaining = timeString(millisUntilFinished)

                timerTextView.text = timeRemaining
            }

            override fun onFinish() {
                timerTextView.text = "SPRING UT FÖR FAN!"

            }
        }
    }

    // Method to get days hours minutes seconds from milliseconds
    fun timeString(millisUntilFinished:Long):String{
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