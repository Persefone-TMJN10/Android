package com.root.bluetoothtester.Bluetooth.Streaming

import android.bluetooth.BluetoothSocket
import java.io.DataInputStream
import java.io.IOException
import java.io.InputStream

class ReadThread constructor(): Thread() {

    var socket: BluetoothSocket? = null
    var stream: DataInputStream? = null
    var isRunning: Boolean? = false
    var inputArray: ArrayList<String>? = null

    constructor(socket: BluetoothSocket): this() {
        inputArray = ArrayList<String>()

        var tmpStream: InputStream? = null

        try {
            tmpStream = socket.inputStream
        } catch (e: IOException) {
            println(e.stackTrace)
        }

        stream = DataInputStream(tmpStream)
    }

    override fun run() {

        var buffer = ByteArray(1024)
        var bytes: Int?

        var msg: String? = String()

        while(isRunning!!) {
            try {
                bytes = stream!!.read(buffer)
                msg += String(buffer, 0, bytes)

                println(msg)

                if(msg!!.split(";").size > 1){
                    inputArray!!.add(msg!!.split(";")[0])
                    msg = ""
                }

            } catch (e: IOException) {
                println(e.stackTrace)
            }

        }

    }

    fun cancel() {
        try {
            socket!!.close()
        } catch (e: IOException) {}

        isRunning = false
    }

}