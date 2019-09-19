package se.ju.lejo.persefone.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import se.ju.lejo.persefone.R
import se.ju.lejo.persefone.Data.RestHandler

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
        //fetchJson()
        postJson()
    }

    fun fetchJson() {
        println("attempting to fetch json...")
        val handler = RestHandler()
        handler.sendGetRequest("http://3.122.218.59/session")
    }

    fun postJson() {
        println("attempting to post json...")
        val handler = RestHandler()
        handler.sendPostRequest("http://3.122.218.59/session/", "2019-09-12 13:13:37", "2019-09-12 13:13:38")
    }
}
