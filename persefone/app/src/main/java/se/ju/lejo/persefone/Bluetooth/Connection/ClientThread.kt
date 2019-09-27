package com.root.bluetoothtester.Bluetooth.Connection

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import java.io.IOException
import java.util.*

class ClientThread constructor(): Thread() {

    companion object {
        const val ACTION_STATUS_CHANGED: String = "ClientThread.action.status.change"
        const val EXTRA_STATUS: String = "ClientThread.extra.status"
        const val STATUS_CONSTRUCTING: Int = 1
        const val STATUS_CONSTRUCTED: Int = 2
        const val STATUS_CONNECTING: Int = 3
        const val STATUS_CONNECTED: Int = 4
    }

    // GENERAL
    private var context: Context? = null
    private var connection: BluetoothConnection? = null

    // BLUETOOTH
    private var device: BluetoothDevice? = null
    private var uuid: UUID? = null

    // BROADCASTING
    private var broadcastIntentActionStatusChanged: Intent? = null

    constructor(context: Context, connection: BluetoothConnection, device: BluetoothDevice, uuid: UUID) : this() {
        this.device = device
        this.connection = connection
        this.uuid = uuid
        this.context = context
        setupReceivers()
    }

    override fun run() {

        var tmpSocket: BluetoothSocket? = null

        try {
            broadcastIntentActionStatusChanged!!.putExtra(EXTRA_STATUS, STATUS_CONSTRUCTING)
            LocalBroadcastManager.getInstance(context!!).sendBroadcast(broadcastIntentActionStatusChanged!!)

            tmpSocket = device!!.createRfcommSocketToServiceRecord(uuid)
        } catch (e: IOException) {}

        broadcastIntentActionStatusChanged!!.putExtra(EXTRA_STATUS, STATUS_CONSTRUCTED)
        LocalBroadcastManager.getInstance(context!!).sendBroadcast(broadcastIntentActionStatusChanged!!)

        BluetoothConnection.socket = tmpSocket

        try {
            broadcastIntentActionStatusChanged!!.putExtra(EXTRA_STATUS, STATUS_CONNECTING)
            LocalBroadcastManager.getInstance(context!!).sendBroadcast(broadcastIntentActionStatusChanged!!)

            BluetoothConnection.socket!!.connect()
        } catch (e: IOException) {}

        broadcastIntentActionStatusChanged!!.putExtra(EXTRA_STATUS, STATUS_CONNECTED)
        LocalBroadcastManager.getInstance(context!!).sendBroadcast(broadcastIntentActionStatusChanged!!)

    }

    fun cancel() {

        try {

            // TODO

        } catch (e: IOException) {}

    }

    private fun setupReceivers() {

        broadcastIntentActionStatusChanged = Intent(ACTION_STATUS_CHANGED)

    }
}
