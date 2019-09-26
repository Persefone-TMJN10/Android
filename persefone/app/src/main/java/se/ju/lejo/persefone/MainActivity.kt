package se.ju.lejo.persefone

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import com.root.bluetoothtester.Bluetooth.Connection.BluetoothConnection
import com.root.bluetoothtester.Bluetooth.Connection.ClientThread
import com.root.bluetoothtester.Bluetooth.Messaging.MessageReader
import com.root.bluetoothtester.Bluetooth.Streaming.ServiceController
import se.ju.lejo.persefone.Bluetooth.BluetoothHandler
import se.ju.lejo.persefone.Fragments.TimerFragment
import java.util.*

class MainActivity : AppCompatActivity() {


    // SERVICE
    var serviceController: ServiceController? = null

    // RECEIVERS
    var receiverClientThreadStatusChanged: BroadcastReceiver? = null
    var receiverBluetoothFoundDevice: BroadcastReceiver? = null

    // BLUETOOTH
    var bluetoothConnection: BluetoothConnection = BluetoothConnection(this)

    // MESSAGING
    var messageReader: MessageReader? = null

    // GUI
    var timerFragment: TimerFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupReceivers()
        serviceController = ServiceController(this)
        startConnection()

        constructFragments()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.root_layout, timerFragment as Fragment, "timer")
            .commit()

    }

    override fun onDestroy() {
        super.onDestroy()

        LocalBroadcastManager.getInstance(baseContext).unregisterReceiver(receiverClientThreadStatusChanged!!)
        baseContext.unregisterReceiver(receiverBluetoothFoundDevice)

    }

    private fun constructFragments() {
        timerFragment = TimerFragment()
    }

    private fun startConnection() {

        BluetoothHandler.confirmBluetoothPermissions(this)
        BluetoothHandler.toggleDiscovery()

    }

    private fun setupReceivers() {

        receiverClientThreadStatusChanged = object: BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent!!.action

                if(action.equals(ClientThread.ACTION_STATUS_CHANGED)) {

                    val extraStatus = intent.getIntExtra(ClientThread.EXTRA_STATUS, 0)

                    when(extraStatus) {

                        ClientThread.STATUS_CONNECTED -> {

                            serviceController!!.startBluetoothStreamService()
                            messageReader = MessageReader()
                            messageReader!!.constructMessageHandler(baseContext)
                            messageReader!!.start()

                        }

                    }

                }
            }

        }

        receiverBluetoothFoundDevice = object: BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {

                val action = intent!!.action

                if(action.equals(BluetoothDevice.ACTION_FOUND)) {

                    val foundDevice: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    if(foundDevice!!.address == "64:A2:F9:F1:83:7A"){

                        bluetoothConnection.startClient(
                            foundDevice,
                            UUID.fromString("c7e390e7-2975-4272-905e-aef4c2099506")
                        )
                    }

                }

            }
        }

        var intentFilterClientThreadStatusChange = IntentFilter(ClientThread.ACTION_STATUS_CHANGED)
        LocalBroadcastManager.getInstance(this.baseContext).registerReceiver(
            receiverClientThreadStatusChanged!!,
            intentFilterClientThreadStatusChange)


        var intentFilterFoundDevice = IntentFilter(BluetoothDevice.ACTION_FOUND)
        baseContext.registerReceiver(receiverBluetoothFoundDevice, intentFilterFoundDevice)

    }
}