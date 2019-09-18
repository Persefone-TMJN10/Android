package se.ju.lejo.persefone.Fragments

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import se.ju.lejo.persefone.R

class BluetoothFragment: Fragment() {

    // VIEWS
    var theView: View? = null
    var btnBluetoothActivation: Button? = null
    var btnFindDevices: Button? = null

    // BLUETOOTH OBJECTS
    var bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    // BROADCASTING OBJECTS
    var receiverBluetoothStatus: BroadcastReceiver? = null
    var recieverDeviceDiscovery: BroadcastReceiver? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (theView == null) {
            theView = inflater.inflate(R.layout.bluetooth_fragment_layout, container, false)
        }

        initViews()
        setOnClickListeners()
        constructRecievers()

        return theView
    }

    private fun initViews(){

        btnBluetoothActivation = theView!!.findViewById(R.id.btn_activate_bluetooth)
        btnFindDevices = theView!!.findViewById(R.id.btn_find_device)

        updateViewsAfterBluetoothStateChange(bluetoothAdapter.isEnabled())

    }

    private fun setOnClickListeners() {

        btnBluetoothActivation!!.setOnClickListener {
            activateBluetooth()
        }

        btnFindDevices!!.setOnClickListener {
            confirmBluetoothPermissions()
            bluetoothAdapter.startDiscovery()
        }

    }

    private fun constructRecievers() {

        receiverBluetoothStatus = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                val action = intent!!.action

                if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){

                    val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)

                    when (state) {

                        BluetoothAdapter.STATE_OFF -> {
                            updateViewsAfterBluetoothStateChange(false)
                        }

                        BluetoothAdapter.STATE_ON -> {
                            updateViewsAfterBluetoothStateChange(true)
                        }

                    }

                }

            }
        }

        recieverDeviceDiscovery = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                val action = intent!!.action

                if(action.equals(BluetoothDevice.ACTION_FOUND)){

                    val foundDevice: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    println("--[New Device]--")
                    println("Name: " + foundDevice!!.name)
                    println("UUID: " + foundDevice.address)

                }

            }
        }

        val intentFilterBluetoothStateChanged = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        activity!!.registerReceiver(receiverBluetoothStatus, intentFilterBluetoothStateChanged)

        val intentFilterDeviceDiscovery = IntentFilter(BluetoothDevice.ACTION_FOUND)
        activity!!.registerReceiver(recieverDeviceDiscovery, intentFilterDeviceDiscovery)

    }

    private fun activateBluetooth() {

        if(!bluetoothAdapter.isEnabled()) {

            var enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivity(enableBtIntent)

        }

    }

    private fun confirmBluetoothPermissions() {

        var permissionCheck = activity!!.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION")
        permissionCheck += activity!!.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION")


        if(permissionCheck != 0){

            activity!!.requestPermissions(
                Array<String>(2) {
                    Manifest.permission.ACCESS_FINE_LOCATION;
                    Manifest.permission.ACCESS_COARSE_LOCATION
                },
                1001
            )

        }

    }

    // newState: true = bluetooth activated , false = bluetooth deactivated

    private fun updateViewsAfterBluetoothStateChange(newState: Boolean?) {

        when (newState) {

            true -> {

                btnBluetoothActivation!!.visibility = View.GONE
                btnFindDevices!!.visibility = View.VISIBLE

            }

            false -> {

                btnBluetoothActivation!!.visibility = View.VISIBLE
                btnFindDevices!!.visibility = View.GONE

            }

        }

    }

}