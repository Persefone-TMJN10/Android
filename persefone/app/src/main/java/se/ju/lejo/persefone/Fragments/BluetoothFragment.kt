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
import se.ju.lejo.persefone.Bluetooth.BluetoothClient
import se.ju.lejo.persefone.R
import java.util.*

class BluetoothFragment: Fragment() {

    // CONSTANTS
    val MY_UUID_INSECURE = UUID.fromString("c7e390e7-2975-4272-905e-aef4c2099506")

    // VIEWS
    var theView: View? = null
    var btnBluetoothActivation: Button? = null
    var btnFindDevices: Button? = null

    // BLUETOOTH OBJECTS
    var bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    var bluetoothClient: BluetoothClient? = null

    // BROADCASTING OBJECTS
    var receiverBluetoothStatus: BroadcastReceiver? = null
    var recieverDeviceDiscovery: BroadcastReceiver? = null
    var recieverBondState: BroadcastReceiver? = null

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

                    if(foundDevice!!.address.equals("98:D3:51:FD:7A:96")) {

                        println("--[Console Found]--")
                        println("NAME: " + foundDevice!!.name)
                        println("BD_ADDR: " + foundDevice.address)

                        if(bluetoothAdapter.getBondedDevices().contains(foundDevice)){
                            startClient(foundDevice)
                        }
                        else {
                            foundDevice.createBond()
                        }

                    }

                }

            }
        }

        recieverBondState = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                var action = intent!!.action

                if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {

                    var bondedDevice: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    when(bondedDevice.bondState) {

                        BluetoothDevice.BOND_BONDED -> {
                            println("Bonded: " + bondedDevice.address)

                            if(bondedDevice.address.equals("98:D3:51:FD:7A:96")){
                                startClient(bondedDevice)
                            }
                        }

                        BluetoothDevice.BOND_BONDING -> {
                            println("Bonding: " + bondedDevice.address)
                        }
                    }

                }

            }
        }

        val intentFilterBluetoothStateChanged = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        activity!!.registerReceiver(receiverBluetoothStatus, intentFilterBluetoothStateChanged)

        val intentFilterDeviceDiscovery = IntentFilter(BluetoothDevice.ACTION_FOUND)
        activity!!.registerReceiver(recieverDeviceDiscovery, intentFilterDeviceDiscovery)

        val intentFilterBondState = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        activity!!.registerReceiver(recieverBondState, intentFilterBondState)

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

    private fun startClient(bondDevice: BluetoothDevice) {

        bluetoothAdapter.cancelDiscovery()

        bluetoothClient = BluetoothClient(activity!!.baseContext)
        bluetoothClient!!.start(bondDevice, MY_UUID_INSECURE)

    }

}