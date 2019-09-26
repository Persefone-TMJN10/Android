package com.root.bluetoothtester.Bluetooth.Messaging

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import se.ju.lejo.persefone.Data.DataHandler
import se.ju.lejo.persefone.Data.RestHandler
import java.text.SimpleDateFormat
import java.util.*

class MessageHandler {

    companion object {
        const val ACTION_MESSAGE_HANDLED: String = "MessageHandler.action.message.handled"
        const val EXTRA_MESSAGE_HANDLED_TYPE: String = "ServerThread.extra.message.handled.type"
        const val TYPE_0: Int = 1
        const val TYPE_1: Int = 2
    }

    // GENERAL
    private var context: Context? = null

    // BROADCASTING
    private var broadcastIntentMessageHandled: Intent? = null

    constructor(context: Context) {
        this.context = context
        setupIntents()
    }

    fun handleMessage(message: List<String>) {

        when(message.get(0)) {

            "0" -> {

                when(message.get(2)) {

                    "0" -> {
                        RestHandler.requestUpdateSession(message.get(1), getTimeString())
                        DataHandler.setIsClockedIn(false)
                    }

                    "1" -> {
                        RestHandler.sendClockInPostRequest(message.get(1), getTimeString())
                        DataHandler.setIsClockedIn(true)
                    }

                }

                broadcastIntentMessageHandled!!.putExtra(EXTRA_MESSAGE_HANDLED_TYPE, TYPE_0)
                LocalBroadcastManager.getInstance(context!!).sendBroadcast(broadcastIntentMessageHandled!!)

            }

            "1" -> {

                DataHandler.setE(message.get(2).toFloat())

                broadcastIntentMessageHandled!!.putExtra(EXTRA_MESSAGE_HANDLED_TYPE, TYPE_1)
                LocalBroadcastManager.getInstance(context!!).sendBroadcast(broadcastIntentMessageHandled!!)

            }

        }

    }

    private fun setupIntents() {
        broadcastIntentMessageHandled = Intent(ACTION_MESSAGE_HANDLED)
    }

    private fun getTimeString(): String {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormatter.format(calendar.time)
    }

}