package se.ju.lejo.persefone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import se.ju.lejo.persefone.Fragments.TimerFragment

class MainActivity : AppCompatActivity() {

    var timerFragment: TimerFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        constructFragments()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.root_layout, timerFragment as Fragment, "timer")
            .commit()

    }

    private fun constructFragments() {
        timerFragment = TimerFragment()
    }
}