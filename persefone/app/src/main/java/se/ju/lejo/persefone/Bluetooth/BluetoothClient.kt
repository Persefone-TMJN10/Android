package se.ju.lejo.persefone.Bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import java.io.IOException
import java.util.*

public class BluetoothClient {

    private var context: Context? = null
    private var clientThread: ClientThread? = null

    companion object {
        var clientSocket: BluetoothSocket? = null
    }

    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var bondingDevice: BluetoothDevice? = null
    private var deviceUuid: UUID? = null

    constructor(context: Context) {
        this.context = context
    }

    @Synchronized public fun start(device: BluetoothDevice, uuid: UUID) {

        if(clientThread != null){
            clientThread!!.cancel()
            clientThread = null
        }

        if(clientThread == null){
            clientThread = ClientThread(device, uuid)
            clientThread!!.start()
        }

    }

    public fun stop() {

    }

    inner class ClientThread constructor(): Thread() {

        constructor(device: BluetoothDevice, uuid: UUID) : this() {
            bondingDevice = device
            deviceUuid = uuid
        }

        override fun run() {

            var tmpSocket: BluetoothSocket? = null

            try {
                println("CreateRfcomm")
                tmpSocket = bondingDevice!!.createRfcommSocketToServiceRecord(bondingDevice!!.uuids.get(0).uuid)
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