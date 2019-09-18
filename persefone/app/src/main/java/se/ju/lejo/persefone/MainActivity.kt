package se.ju.lejo.persefone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Button
import se.ju.lejo.persefone.Fragments.BluetoothFragment
import se.ju.lejo.persefone.Fragments.TimerFragment

class MainActivity : AppCompatActivity() {

    private val fragmentManager: FragmentManager = supportFragmentManager
    private var fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

    private var layoutNav: View? = null
    private var btnTimer: Button? = null
    private var btnBluetooth: Button? = null

    private var timerFragment: TimerFragment? = null
    private val TIMER_FRAGMENT_ID = "id_fragment_timer"
    private var bluetoothFragment: BluetoothFragment? = null
    private val BLUETOOTH_FRAGMENT_ID = "id_fragment_bluetooth"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTimer = findViewById(R.id.btn_timer)
        btnBluetooth = findViewById(R.id.btn_bluetooth)
        layoutNav = findViewById(R.id.nav_layout)

        setOnClickListeners()

        constructFragments()
        addRootFragment()

    }

    private fun setOnClickListeners() {

        btnTimer!!.setOnClickListener {

            layoutNav!!.setBackgroundColor(
                ContextCompat.getColor(this, R.color.colorSecondary)
            )

            btnTimer!!.setBackgroundColor(
                ContextCompat.getColor(this, R.color.colorPrimary)
            )

            btnTimer!!.setTextColor(
                ContextCompat.getColor(this, R.color.onPrimary)
            )

            btnBluetooth!!.setBackgroundColor(
                ContextCompat.getColor(this, R.color.colorPrimary)
            )

            btnBluetooth!!.setTextColor(
                ContextCompat.getColor(this, R.color.onPrimary)
            )

            fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.hide(
                fragmentManager.findFragmentByTag(BLUETOOTH_FRAGMENT_ID)!!
            )

            fragmentTransaction.show(
                fragmentManager.findFragmentByTag(TIMER_FRAGMENT_ID)!!
            )

            fragmentTransaction.commit()

        }

        btnBluetooth!!.setOnClickListener {


            layoutNav!!.setBackgroundColor(
                ContextCompat.getColor(this, R.color.colorPrimary)
            )

            btnTimer!!.setBackgroundColor(
                ContextCompat.getColor(this, R.color.colorSecondary)
            )

            btnTimer!!.setTextColor(
                ContextCompat.getColor(this, R.color.onSecondary)
            )

            btnBluetooth!!.setBackgroundColor(
                ContextCompat.getColor(this, R.color.colorSecondary)
            )

            btnBluetooth!!.setTextColor(
                ContextCompat.getColor(this, R.color.onSecondary)
            )

            fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.hide(
                fragmentManager.findFragmentByTag(TIMER_FRAGMENT_ID)!!
            )

            fragmentTransaction.show(
                fragmentManager.findFragmentByTag(BLUETOOTH_FRAGMENT_ID)!!
            )

            fragmentTransaction.commit()

        }

    }

    private fun constructFragments() {
        timerFragment = TimerFragment()
        bluetoothFragment = BluetoothFragment()
    }

    private fun addRootFragment() {
        fragmentTransaction.add(R.id.fragment_layout, timerFragment as Fragment, TIMER_FRAGMENT_ID)
        fragmentTransaction.add(R.id.fragment_layout, bluetoothFragment as Fragment, BLUETOOTH_FRAGMENT_ID)
        fragmentTransaction.commit()
    }
}