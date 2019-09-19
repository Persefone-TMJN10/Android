package se.ju.lejo.persefone.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import se.ju.lejo.persefone.Bluetooth.BluetoothHandler
import se.ju.lejo.persefone.R
import se.ju.lejo.persefone.Data.RestHandler

class TimerFragment: Fragment() {

    private var theView: View? = null
    private var bluetoothHandler: BluetoothHandler? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        bluetoothHandler = BluetoothHandler(activity!!)

        if (theView == null) {
            theView = inflater.inflate(R.layout.timer_fragment_layout, container, false)
        }
        return theView
    }

    override fun onResume() {
        super.onResume()
        //fetchJson()
        //clockIn()
    }

    fun fetchJson() {
        println("attempting to fetch json...")
        val handler = RestHandler()
        handler.sendGetRequest()
    }

    fun clockIn() {
        println("attempting to post json...")
        val handler = RestHandler()
        handler.sendClockInPostRequest("2019-09-12 13:13:10")
    }
}
