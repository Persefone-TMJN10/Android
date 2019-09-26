package se.ju.lejo.persefone.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import se.ju.lejo.persefone.Bluetooth.BluetoothHandler
import se.ju.lejo.persefone.GottOchBlandat.RadiationTracker
import se.ju.lejo.persefone.MainActivity
import se.ju.lejo.persefone.R
import se.ju.lejo.persefone.Time.Timer


class TimerFragment: Fragment() {

    private var _theView: View? = null
    private var _radTracker: RadiationTracker = RadiationTracker()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        if (_theView == null) {
            _theView = inflater.inflate(R.layout.timer_fragment_layout, container, false)
        }

        val timerTextView = _theView?.findViewById(R.id.timerTV) as TextView

        val timer = Timer(timerTextView, context)

        //TODO: implement broadcast Receiver to listen for changes ( of E )
        //region inside receiver

        timer.timerCountDown(_radTracker.calculateTimeLeft()).start()

        //endregion

        return _theView
    }
}
