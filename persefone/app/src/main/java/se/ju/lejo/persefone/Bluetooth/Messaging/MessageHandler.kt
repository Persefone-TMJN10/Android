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
        const val TYPE_2: Int = 3
        const val TYPE_3: Int = 4
        const val TYPE_4: Int = 5
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

                // Clock in with RFID and start environment values (radiation value, hazmat status and room id)
                // Recalculate E based on new R, rc and pc
                // Send broadcast
                // Push session to DB
                // Push startEnvironment to DB

                DataHandler.setIsClockedIn(true)

                broadcastIntentMessageHandled!!.putExtra(EXTRA_MESSAGE_HANDLED_TYPE, TYPE_0)
                LocalBroadcastManager.getInstance(context!!).sendBroadcast(broadcastIntentMessageHandled!!)

                RestHandler.sendClockInPostRequest(message.get(1), getTimeString())

            }

            "1" -> {

                // Clock out with RFID
                // Send broadcast
                // Push PUT-request to REST-API

                DataHandler.setIsClockedIn(false)

                broadcastIntentMessageHandled!!.putExtra(EXTRA_MESSAGE_HANDLED_TYPE, TYPE_1)
                LocalBroadcastManager.getInstance(context!!).sendBroadcast(broadcastIntentMessageHandled!!)

                RestHandler.requestUpdateSession(message.get(1), getTimeString())

            }

            "2" -> {
                // New radVal (0 >= R <= 100)
                // Recalculate E based on new R
                // Send broadcast
                // Push radiationLevelChange to DB

                DataHandler.setE(message.get(2).toFloat())

                broadcastIntentMessageHandled!!.putExtra(EXTRA_MESSAGE_HANDLED_TYPE, TYPE_2)
                LocalBroadcastManager.getInstance(context!!).sendBroadcast(broadcastIntentMessageHandled!!)
            }

            "3" -> {
                // New hazmat status (0 = off, 1 = true)
                // Recalculate E based on new pc (protective coefficient)
                // Send broadcast
                // Push hazmatChange to DB

                broadcastIntentMessageHandled!!.putExtra(EXTRA_MESSAGE_HANDLED_TYPE, TYPE_3)
                LocalBroadcastManager.getInstance(context!!).sendBroadcast(broadcastIntentMessageHandled!!)
            }

            "4" -> {
                // New room id {0,1,2}
                // Recalculate E based on new rc (room coefficient)
                // Send broadcast
                // Push roomChange to DB

                broadcastIntentMessageHandled!!.putExtra(EXTRA_MESSAGE_HANDLED_TYPE, TYPE_4)
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
