package se.ju.lejo.persefone.Fragments

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import se.ju.lejo.persefone.Bluetooth.BluetoothHandler
import se.ju.lejo.persefone.MainActivity
import se.ju.lejo.persefone.R

class ConnectToBTFragment: Fragment() {

    companion object {
        const val TAG = "tag_connectToBT_fragment"

        enum class States {
            ACTIVATE_BT,
            CONNECT_BT,
            LOADING_SCREEN
        }
    }

    private var theView: View? = null
    private var activateBluetooth: TextView? = null
    private var connetBluetooth: TextView? = null
    private var loadingView: View? = null
    private var loadingText: TextView? = null
    private var receiverBluetoothStateChange: BroadcastReceiver? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        if (theView == null) {
            theView = inflater.inflate(R.layout.connect_to_bt_fragment, container, false)
        }


        return theView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findViews()

        if (BluetoothHandler.isEnabled()) {
            setUpView(States.CONNECT_BT)
        } else {
            setUpView(States.ACTIVATE_BT)
        }

        setUpReceiver()

        activateBluetooth?.setOnClickListener {
            BluetoothHandler.toggleBluetooth()
        }

        connetBluetooth?.setOnClickListener {
            (activity as MainActivity).startConnection()
            setUpView(States.LOADING_SCREEN)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(receiverBluetoothStateChange)
    }

    private fun findViews() {

        activateBluetooth = theView?.findViewById(R.id.btn_activateBT)
        connetBluetooth = theView?.findViewById(R.id.btn_connectBT)
        loadingView = theView?.findViewById(R.id.ll_loadingScreen)
        loadingText = theView?.findViewById(R.id.tv_loadingText)
    }

    private fun setUpView(state: States) {

        when(state) {
            States.ACTIVATE_BT -> {
                activateBluetooth?.visibility = View.VISIBLE
                connetBluetooth?.visibility = View.GONE
                loadingView?.visibility = View.GONE
            }
            States.CONNECT_BT -> {
                activateBluetooth?.visibility = View.GONE
                connetBluetooth?.visibility = View.VISIBLE
                loadingView?.visibility = View.GONE
            }
            States.LOADING_SCREEN -> {
                activateBluetooth?.visibility = View.GONE
                connetBluetooth?.visibility = View.GONE
                loadingView?.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpReceiver() {

        receiverBluetoothStateChange = object : BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action

                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {

                    val state = intent?.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)

                    when (state) {

                        BluetoothAdapter.STATE_OFF -> {
                            setUpView(States.ACTIVATE_BT)
                        }

                        BluetoothAdapter.STATE_ON -> {
                            setUpView(States.CONNECT_BT)
                        }
                    }
                }
            }
        }

        val intentFilterBluetoothStateChange = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        activity!!.baseContext.registerReceiver(receiverBluetoothStateChange, intentFilterBluetoothStateChange)
    }
}