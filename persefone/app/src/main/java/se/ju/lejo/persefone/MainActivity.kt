package se.ju.lejo.persefone

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.root.bluetoothtester.Bluetooth.Connection.BluetoothConnection
import com.root.bluetoothtester.Bluetooth.Connection.ClientThread
import com.root.bluetoothtester.Bluetooth.Messaging.MessageReader
import com.root.bluetoothtester.Bluetooth.Streaming.ServiceController
import se.ju.lejo.persefone.Adapter.RecycleViewAdapter
import se.ju.lejo.persefone.Bluetooth.BluetoothHandler
import se.ju.lejo.persefone.Fragments.ConnectToBTFragment
import se.ju.lejo.persefone.Fragments.HistoryFragment
import se.ju.lejo.persefone.Fragments.TimerFragment
import se.ju.lejo.persefone.Models.HistoryList
import java.util.*
import kotlin.collections.ArrayList

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
    var connectToBTFragment: ConnectToBTFragment? = null
    var historyFragment: HistoryFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupReceivers()
        serviceController = ServiceController(this)

        constructFragments()

        // LIST VIEW
        val recyclerView = findViewById<RecyclerView>(R.id.history_recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        val historyList = ArrayList<HistoryList>()
        historyList.add(HistoryList("2019-10-02 22:33:57", "2019-10-02 22:33:57", 30))
        historyList.add(HistoryList("2019-10-03 22:33:57", "2019-10-03 22:33:57", 40))
        historyList.add(HistoryList("2019-10-04 22:33:57", "2019-10-04 22:33:57", 90))

        val recycleViewAdapter = RecycleViewAdapter(historyList)
        recyclerView?.adapter = recycleViewAdapter


        //changed here for main fragment
        supportFragmentManager
            .beginTransaction()
            .add(R.id.root_layout, historyFragment as Fragment, HistoryFragment.TAG)
            .commit()

    }

    override fun onDestroy() {
        super.onDestroy()

        LocalBroadcastManager.getInstance(baseContext).unregisterReceiver(receiverClientThreadStatusChanged!!)
        baseContext.unregisterReceiver(receiverBluetoothFoundDevice)

    }

    private fun constructFragments() {
        timerFragment = TimerFragment()
        connectToBTFragment = ConnectToBTFragment()
        historyFragment = HistoryFragment()
    }

    fun startConnection() {
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

                            supportFragmentManager
                                .beginTransaction()
                                .replace(R.id.root_layout, timerFragment as Fragment, TimerFragment.TAG)
                                .commit()

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

                    if(foundDevice!!.address == "98:D3:51:FD:7A:96"/*"64:A2:F9:F1:83:7A"*/){

                        bluetoothConnection.startClient(
                            foundDevice,
                            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"/*"c7e390e7-2975-4272-905e-aef4c2099506"*/)
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