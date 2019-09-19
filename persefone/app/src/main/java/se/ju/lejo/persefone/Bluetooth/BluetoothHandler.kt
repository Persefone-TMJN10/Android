package se.ju.lejo.persefone.Bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class BluetoothHandler {

    var mainActivity: Activity? = null
    var bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    var bluetoothClient: BluetoothClient? = null

    // BROADCASTING OBJECTS
    var receiverBluetoothStatus: BroadcastReceiver? = null
    var recieverDeviceDiscovery: BroadcastReceiver? = null
    var recieverBondState: BroadcastReceiver? = null

    constructor(mainActivity: Activity) {
        this.mainActivity = mainActivity

        setupReceivers()
        confirmBluetoothPermissions()
        bluetoothAdapter.startDiscovery()
    }

    private fun setupReceivers() {

        receiverBluetoothStatus = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                val action = intent!!.action

                if(action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){

                    val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)

                    when (state) {

                        BluetoothAdapter.STATE_OFF -> {
                            // Do something
                        }

                        BluetoothAdapter.STATE_ON -> {
                            // Do something
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
        mainActivity!!.baseContext.registerReceiver(receiverBluetoothStatus, intentFilterBluetoothStateChanged)

        val intentFilterDeviceDiscovery = IntentFilter(BluetoothDevice.ACTION_FOUND)
        mainActivity!!.baseContext.registerReceiver(recieverDeviceDiscovery, intentFilterDeviceDiscovery)

        val intentFilterBondState = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        mainActivity!!.baseContext.registerReceiver(recieverBondState, intentFilterBondState)
    }

    private fun startClient(bondedDevice: BluetoothDevice) {

        bluetoothAdapter.cancelDiscovery()

        bluetoothClient = BluetoothClient(mainActivity!!.baseContext)
        bluetoothClient!!.start(bondedDevice)

    }

    fun checkBluetoothStatus(): Boolean {

        return bluetoothAdapter!!.isEnabled()

    }

    private fun activateBluetooth() {

        if(!bluetoothAdapter.isEnabled()) {

            var enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            mainActivity!!.baseContext.startActivity(enableBtIntent)

        }

    }

    private fun confirmBluetoothPermissions() {

        var permissionCheck = mainActivity!!.baseContext.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION")
        permissionCheck += mainActivity!!.baseContext.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION")


        if(permissionCheck != 0){
            mainActivity!!.requestPermissions(
                Array<String>(2) {
                    Manifest.permission.ACCESS_FINE_LOCATION;
                    Manifest.permission.ACCESS_COARSE_LOCATION
                },
                1001
            )

        }

    }

}