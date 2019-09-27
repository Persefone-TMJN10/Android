package com.root.bluetoothtester.Bluetooth.Streaming

import android.app.Service
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager.*
import com.root.bluetoothtester.Bluetooth.Connection.BluetoothConnection
import java.io.IOException
import java.io.OutputStream

class BluetoothStreamService: Service() {

    companion object {
        const val ACTION_STATUS_CHANGED: String = "BluetoothStreamService.action.status.change"
        const val EXTRA_STATUS: String = "BluetoothStreamService.extra.status"
        const val STATUS_STARTED = 1
    }

    // STREAMING
    private var readThread: ReadThread? = null
    private var outputStream: OutputStream? = null

    // INTENTS
    private var broadcastIntentStatusChange: Intent = Intent(ACTION_STATUS_CHANGED)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        try {
            outputStream = BluetoothConnection.socket!!.outputStream
        } catch (e: IOException) {
            println(e.stackTrace)
        }

        readThread = ReadThread(BluetoothConnection.socket as BluetoothSocket)
        readThread!!.isRunning = true
        readThread!!.start()

        broadcastIntentStatusChange.putExtra(EXTRA_STATUS, STATUS_STARTED)
        getInstance(this).sendBroadcast(broadcastIntentStatusChange)

        return START_NOT_STICKY

    }

    fun write(message: String) {

        try {
            outputStream!!.write((message + ";").toByteArray())
        } catch (e: IOException) {
            println(e.stackTrace)
        }

    }

    fun read(): String? {

        if(readThread!!.inputArray!!.size > 0) {
           return readThread!!.inputArray!!.removeAt(0)
        }
        else return null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return StreamBinder()
    }

    inner class StreamBinder constructor(): Binder() {

        fun getService(): BluetoothStreamService {
            return this@BluetoothStreamService
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        readThread!!.cancel()

        var retry: Boolean = true

        while(retry) {

            try {
                readThread!!.join()
            } catch (e: IOException) {
                println(e.stackTrace)
            }

            retry = false

        }
    }
}