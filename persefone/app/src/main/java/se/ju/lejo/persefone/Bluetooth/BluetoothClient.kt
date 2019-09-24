package se.ju.lejo.persefone.Bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import java.io.IOException
import java.util.*

public class BluetoothClient {

    private var context: Context? = null
    private var clientThread: ClientThread? = null

    companion object {
        var clientSocket: BluetoothSocket? = null
        val uuid: UUID? = java.util.UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }

    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var bondingDevice: BluetoothDevice? = null

    constructor(context: Context) {
        this.context = context
    }

    @Synchronized public fun start(device: BluetoothDevice) {

        if(clientThread != null){
            clientThread!!.cancel()
            clientThread = null
        }

        if(clientThread == null){
            clientThread = ClientThread(device)
            clientThread!!.start()
        }

    }

    public fun stop() {

    }

    inner class ClientThread constructor(): Thread() {

        val ACTION_CONNECTION_CHANGE: String = "BluetoothClient.ClientThread.Connection.Change"
        val EXTRA_CONNECTION_CHANGE: String = "BluetoothClient.ClientThread.Extra.Connection.Change"
        val CHANGE_RFCOMM_CREATED: Int = 0
        val CHANGE_RFCOMM_FAILURE: Int = 1
        val CHANGE_SOCKET_CONNECTED: Int = 2
        val CHANGE_SOCKET_FAILURE: Int = 3

        private var broadcast_creating_rfcomm: Intent? = null
        private var broadcast_failure_rfcomm: Intent? = null
        private var broadcast_connecting_socket: Intent? = null
        private var broadcast_failure_socket: Intent? = null



        constructor(device: BluetoothDevice) : this() {
            bondingDevice = device
        }

        override fun run() {

            var tmpSocket: BluetoothSocket? = null

            try {
                tmpSocket = bondingDevice!!.createRfcommSocketToServiceRecord(uuid)

            } catch (e: IOException) {
            }

            clientSocket = tmpSocket

            try {
                println("Connect Socket")
                clientSocket!!.connect()
            } catch (e: IOException) {

                println("Socket fallback")


                println(e.message)
                println("Try closing socket if connection failed")

            }


            connected()

        }

        public fun cancel() {

            try {
                println("Closing socket")
            } catch (e: IOException) {
                println("Cancelation Error")
            }

        }

    }

    private fun connected() {

        println("Start connection service")

        var intentStartBluetoothService = Intent(context, BluetoothService::class.java)

        context!!.startService(intentStartBluetoothService)
    }

}