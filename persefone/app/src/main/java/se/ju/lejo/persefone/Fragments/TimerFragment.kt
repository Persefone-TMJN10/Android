package se.ju.lejo.persefone.Fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.root.bluetoothtester.Bluetooth.Messaging.MessageHandler
import se.ju.lejo.persefone.Data.DataHandler
import se.ju.lejo.persefone.GottOchBlandat.RadiationTracker
import se.ju.lejo.persefone.R
import se.ju.lejo.persefone.Time.Timer


class TimerFragment: Fragment() {

    companion object {
        const val TAG = "tag_timer_fragment"
    }

    private var _theView: View? = null
    private var _radTracker: RadiationTracker = RadiationTracker()
    private var receiverMessageHandled: BroadcastReceiver? = null
    private var timer: Timer? = null
    private var timerTextView: TextView? = null
    private var redLight: ImageView? = null
    private var greenLight: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        if (_theView == null) {
            _theView = inflater.inflate(R.layout.timer_fragment_layout, container, false)
        }

        setUpViews()

        timer = Timer(timerTextView!!, context)

        setUpReceiver()

        return _theView
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(receiverMessageHandled!!)
    }

    private fun setUpViews() {
        timerTextView = _theView?.findViewById(R.id.timerTV)
        redLight = _theView?.findViewById(R.id.IV_redLigth)
        greenLight = _theView?.findViewById(R.id.IV_greenLight)
    }

    private fun setUpReceiver() {

        receiverMessageHandled = object : BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action

                if (action.equals(MessageHandler.ACTION_MESSAGE_HANDLED)) {

                    val extra = intent?.getIntExtra(MessageHandler.EXTRA_MESSAGE_HANDLED_TYPE, 0)

                    when (extra) {

                        MessageHandler.TYPE_0 -> {

                             if (DataHandler.getIsClockedIn()) {

                                 timer?.startTimer(_radTracker.calculateTimeLeft())

                                 greenLight?.setBackgroundColor(resources.getColor(R.color.green))
                                 redLight?.setBackgroundColor(resources.getColor(R.color.darkRed))
                             } else {

                                 timer?.stopTimer()

                                 greenLight?.setBackgroundColor(resources.getColor(R.color.darkGreen))
                                 redLight?.setBackgroundColor(resources.getColor(R.color.red))
                             }
                        }

                        MessageHandler.TYPE_1 -> {
                            timer?.stopTimer()
                            timer?.startTimer(_radTracker.calculateTimeLeft())
                        }
                    }
                }
            }
        }

        val intentFilterMessageHandled = IntentFilter(MessageHandler.ACTION_MESSAGE_HANDLED)
        LocalBroadcastManager.getInstance(context!!).registerReceiver(receiverMessageHandled!!, intentFilterMessageHandled)
    }
}
