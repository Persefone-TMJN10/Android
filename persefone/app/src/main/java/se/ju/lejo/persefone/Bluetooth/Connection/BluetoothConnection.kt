package com.root.bluetoothtester.Bluetooth.Connection

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import com.root.bluetoothtester.Bluetooth.Streaming.BluetoothStreamService
import java.util.*

class BluetoothConnection {

    companion object {
        var socket: BluetoothSocket? = null
    }

    private var context: Context? = null
    private var clientThread: ClientThread? = null

    constructor(context: Context) {
        this.context = context
    }

    @Synchronized fun startClient(device: BluetoothDevice, uuid: UUID) {

        if(clientThread != null){
            clientThread!!.cancel()
            clientThread = null
        }

        if(clientThread == null){
            clientThread = ClientThread(context!!, this, device, uuid)
            clientThread!!.start()
        }

    }

}