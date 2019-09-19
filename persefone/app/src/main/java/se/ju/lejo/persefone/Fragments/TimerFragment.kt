package se.ju.lejo.persefone.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import se.ju.lejo.persefone.Bluetooth.BluetoothHandler
import se.ju.lejo.persefone.Data.RestHandler
import se.ju.lejo.persefone.Models.Session
import se.ju.lejo.persefone.R

class TimerFragment: Fragment() {

    private var theView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        if (theView == null) {
            theView = inflater.inflate(R.layout.timer_fragment_layout, container, false)
        }
        return theView
    }

    override fun onResume() {
        super.onResume()
        //clockIn()
        updateSession()
    }

    fun fetchJson() {
        println("attempting to fetch json...")
        val handler = RestHandler()
        handler.sendGetRequest()
    }

    fun clockIn() {
        println("attempting to post json...")
        val handler = RestHandler()
        handler.sendClockInPostRequest("000000","2019-09-12 13:00:10")
    }

    fun updateSession() {
        var session = Session()
        session.tagId = "000000"
        session.inTime = "2019-09-12 13:13:10"
        session.outTime = "2019-09-12 14:00:00"

        val handler = RestHandler()
        handler.requestUpdateSession(session)
    }
}
