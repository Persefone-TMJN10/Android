package se.ju.lejo.persefone.Fragments

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.root.bluetoothtester.Bluetooth.Connection.ClientThread
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
    private var connectBluetooth: TextView? = null
    private var loadingView: View? = null
    private var loadingText: TextView? = null
    private var receiverBluetoothStateChange: BroadcastReceiver? = null
    private var receiverBluetoothClientStatusChange: BroadcastReceiver? = null
    private var receiverBluetoothDiscoverStarted: BroadcastReceiver? = null
    private var receiverBluetoothFoundDevice: BroadcastReceiver? = null
    private var receiverClientThreadStatusChanged: BroadcastReceiver? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        if (theView == null) {
            theView = inflater.inflate(R.layout.connect_to_bt_fragment, container, false)
        }

        connectBluetooth?.isEnabled = true

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

        connectBluetooth?.setOnClickListener {
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
        connectBluetooth = theView?.findViewById(R.id.btn_connectBT)
        loadingView = theView?.findViewById(R.id.ll_loadingScreen)
        loadingText = theView?.findViewById(R.id.tv_loadingText)
    }

    private fun setUpView(state: States) {

        when(state) {
            States.ACTIVATE_BT -> {
                activateBluetooth?.visibility = View.VISIBLE
                connectBluetooth?.visibility = View.GONE
                loadingView?.visibility = View.GONE
            }
            States.CONNECT_BT -> {
                activateBluetooth?.visibility = View.GONE
                connectBluetooth?.visibility = View.VISIBLE
                loadingView?.visibility = View.GONE
            }
            States.LOADING_SCREEN -> {
                activateBluetooth?.visibility = View.GONE
                connectBluetooth?.visibility = View.GONE
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

        receiverBluetoothClientStatusChange = object: BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {

                val action = intent!!.action

                if(action.equals(ClientThread.ACTION_STATUS_CHANGED)) {

                    val extraStatus: Int = intent.getIntExtra(ClientThread.EXTRA_STATUS, 0)

                    when(extraStatus) {

                        ClientThread.STATUS_CONSTRUCTING -> loadingText!!.text = "Constructing connection"

                        ClientThread.STATUS_CONSTRUCTED -> loadingText!!.text = "Connection constructed"

                        ClientThread.STATUS_CONNECTING -> loadingText!!.text = "Connecting to console"

                        ClientThread.STATUS_CONNECTED -> {
                            loadingText!!.text = "Console connected"
                        }

                    }

                }

            }
        }

        receiverBluetoothDiscoverStarted = object: BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {

                val action = intent!!.action

                if(action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)){

                    loadingText!!.text = "Searching for console"

                }

            }

        }

        receiverBluetoothFoundDevice = object: BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {

                val action = intent!!.action

                if(action.equals(BluetoothDevice.ACTION_FOUND)) {

                    val foundDevice: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    if(foundDevice!!.address == "64:A2:F9:F1:83:7A"){
                        loadingText!!.text = "Console found"
                    }

                }

            }
        }


        receiverClientThreadStatusChanged = object: BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent!!.action

                if(action.equals(ClientThread.ACTION_STATUS_CHANGED)) {

                    val extraStatus = intent.getIntExtra(ClientThread.EXTRA_STATUS, 0)

                    when(extraStatus) {

                        ClientThread.STATUS_CONNECTED -> {

                            connectBluetooth?.isEnabled = false

                        }
                    }
                }
            }
        }

        val intentFilterBluetoothStateChange = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        activity!!.baseContext.registerReceiver(receiverBluetoothStateChange, intentFilterBluetoothStateChange)

        var intentFilterBluetoothClientStatusChanged = IntentFilter(ClientThread.ACTION_STATUS_CHANGED)
        LocalBroadcastManager.getInstance(activity!!.baseContext).registerReceiver(
            receiverBluetoothClientStatusChange!!,
            intentFilterBluetoothClientStatusChanged
        )


        var intentFilterClientThreadStatusChange = IntentFilter(ClientThread.ACTION_STATUS_CHANGED)
        LocalBroadcastManager.getInstance(activity!!.baseContext).registerReceiver(
            receiverClientThreadStatusChanged!!,
            intentFilterClientThreadStatusChange)


        var intentFilterDiscoveryStarted = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        activity!!.baseContext.registerReceiver(receiverBluetoothDiscoverStarted, intentFilterDiscoveryStarted)

        var intentFilterFoundDevice = IntentFilter(BluetoothDevice.ACTION_FOUND)
        activity!!.baseContext.registerReceiver(receiverBluetoothFoundDevice, intentFilterFoundDevice)
    }
}