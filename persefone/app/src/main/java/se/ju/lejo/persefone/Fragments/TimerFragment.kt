package se.ju.lejo.persefone.Fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.root.bluetoothtester.Bluetooth.Messaging.MessageHandler
import se.ju.lejo.persefone.GottOchBlandat.RadiationTracker
import se.ju.lejo.persefone.R
import se.ju.lejo.persefone.Time.Timer


class TimerFragment: Fragment() {

    companion object {
        const val TAG = "tag_timer_fragment"
    }

    private var theView: View? = null
    private var radTracker: RadiationTracker = RadiationTracker()
    private var receiverMessageHandled: BroadcastReceiver? = null
    private var timer: Timer? = null
    private var timerTextView: TextView? = null
    private var redLight: ImageView? = null
    private var greenLight: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        if (theView == null) {
            theView = inflater.inflate(R.layout.timer_fragment_layout, container, false)
        }

        setUpViews()

        timer = Timer(timerTextView!!, context)

        setUpReceiver()

        return theView
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(receiverMessageHandled!!)
    }

    private fun setUpViews() {
        timerTextView = theView?.findViewById(R.id.timerTV)
        redLight = theView?.findViewById(R.id.IV_redLigth)
        greenLight = theView?.findViewById(R.id.IV_greenLight)
    }

    private fun setUpReceiver() {

        receiverMessageHandled = object : BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action

                if (action.equals(MessageHandler.ACTION_MESSAGE_HANDLED)) {

                    val extra = intent?.getIntExtra(MessageHandler.EXTRA_MESSAGE_HANDLED_TYPE, 0)

                    when (extra) {

                        MessageHandler.TYPE_0 -> {
                             timer?.startTimer(radTracker.calculateTimeLeft())
                             greenLight?.background?.setTint(ContextCompat.getColor(context!!, R.color.green))
                             redLight?.background?.setTint(ContextCompat.getColor(context!!, R.color.darkRed))
                        }

                        MessageHandler.TYPE_1 -> {
                            timer?.stopTimer()
                            greenLight?.background?.setTint(ContextCompat.getColor(context!!, R.color.darkGreen))
                            redLight?.background?.setTint(ContextCompat.getColor(context!!, R.color.red))
                        }

                        MessageHandler.TYPE_2 -> {
                            timer?.stopTimer()
                            timer?.startTimer(radTracker.calculateTimeLeft())
                        }

                        MessageHandler.TYPE_3 -> {
                            timer?.stopTimer()
                            timer?.startTimer(radTracker.calculateTimeLeft())
                        }

                        MessageHandler.TYPE_4 -> {
                            timer?.stopTimer()
                            timer?.startTimer(radTracker.calculateTimeLeft())
                        }
                    }
                }
            }
        }

        val intentFilterMessageHandled = IntentFilter(MessageHandler.ACTION_MESSAGE_HANDLED)
        LocalBroadcastManager.getInstance(context!!).registerReceiver(receiverMessageHandled!!, intentFilterMessageHandled)
    }
}
