package com.root.bluetoothtester.Bluetooth.Streaming

import android.app.Activity
import android.content.*
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager

class ServiceController {

    // GENERAL
    private var activity: Activity? = null

    // RECEIVERS
    private var receiverServiceStatusChange: BroadcastReceiver? = null

    // SERVICE
    private var serviceConnection: ServiceConnection? = null

    companion object {

        private var streamServiceBinder: BluetoothStreamService.StreamBinder? = null

        fun getService(): BluetoothStreamService? {

            if(streamServiceBinder == null)
                return null

            return streamServiceBinder!!.getService()

        }

    }

    constructor(activity: Activity) {
        this.activity = activity
        setupReceivers()
    }

    fun finalize() {
        LocalBroadcastManager.getInstance(activity!!.baseContext).unregisterReceiver(receiverServiceStatusChange!!)
    }

    fun startBluetoothStreamService() {


        val intentStartBluetoothStreamService = Intent(activity!!.baseContext, BluetoothStreamService::class.java)

        activity!!.baseContext.startService(intentStartBluetoothStreamService)

    }

    private fun setupReceivers() {

        receiverServiceStatusChange = object: BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {

                val action = intent!!.action

                if(action.equals(BluetoothStreamService.ACTION_STATUS_CHANGED)) {

                    val extraStatus: Int = intent.getIntExtra(BluetoothStreamService.EXTRA_STATUS, 0)

                    when(extraStatus) {

                        BluetoothStreamService.STATUS_STARTED -> setupConnection()

                    }

                }
            }

        }

        var intentFilterServiceStatusChange = IntentFilter(BluetoothStreamService.ACTION_STATUS_CHANGED)
        LocalBroadcastManager.getInstance(activity!!.baseContext).registerReceiver(
            receiverServiceStatusChange!!,
            intentFilterServiceStatusChange)

    }

    private fun setupConnection() {

        serviceConnection = object: ServiceConnection {

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {

                streamServiceBinder = service as BluetoothStreamService.StreamBinder

            }

            override fun onServiceDisconnected(name: ComponentName?) {

            }

        }

        var intentBindService = Intent(activity!!.baseContext, BluetoothStreamService::class.java)
        activity!!.baseContext.bindService(intentBindService, serviceConnection, Context.BIND_NOT_FOREGROUND)

    }

}