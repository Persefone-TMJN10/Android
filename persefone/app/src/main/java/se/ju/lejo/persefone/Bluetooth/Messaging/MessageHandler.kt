package com.root.bluetoothtester.Bluetooth.Messaging

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import se.ju.lejo.persefone.Data.DataHandler
import se.ju.lejo.persefone.Data.Resources.HazmatChange
import se.ju.lejo.persefone.Data.Resources.RadiationLevelChange
import se.ju.lejo.persefone.Data.Resources.RoomChange
import se.ju.lejo.persefone.Data.Resources.StartEnvironment
import se.ju.lejo.persefone.Data.RestHandler
import se.ju.lejo.persefone.Fragments.HistoryFragment
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

                DataHandler.setIsClockedIn(true)
                DataHandler.setRfId(message.get(1))
                DataHandler.setR(message.get(2).toFloat().toInt())
                DataHandler.setPcFactor(message.get(3).toFloat().toInt())
                DataHandler.calculatePc()
                DataHandler.setRc(message.get(4).toFloat())
                DataHandler.calculateE()

                broadcastIntentMessageHandled!!.putExtra(EXTRA_MESSAGE_HANDLED_TYPE, TYPE_0)
                LocalBroadcastManager.getInstance(context!!).sendBroadcast(broadcastIntentMessageHandled!!)

                RestHandler.postSession(message.get(1), getTimeString()) {
                    if(it) {

                        // ENVIRONMENT STARTUP
                        val startEnvironment = StartEnvironment(
                            DataHandler.getCurrentSessionId()!!,
                            message.get(2).toFloat().toInt(),
                            message.get(3).toFloat().toInt(),
                            message.get(4).toFloat().toInt()
                        )

                        RestHandler.postStartEnvironment(startEnvironment)
                    }
                }

            }

            "1" -> {

                // Clock out
                val totalExposure = DataHandler.getET()

                DataHandler.setIsClockedIn(false)
                DataHandler.setET(0f)

                broadcastIntentMessageHandled!!.putExtra(EXTRA_MESSAGE_HANDLED_TYPE, TYPE_1)
                LocalBroadcastManager.getInstance(context!!).sendBroadcast(broadcastIntentMessageHandled!!)

                RestHandler.putSession(DataHandler.getCurrentSessionId()!!, getTimeString(), totalExposure!!.toLong())

            }

            "2" -> {
                // New radVal (0 >= R <= 100)
                if (DataHandler.getIsClockedIn()) {

                    DataHandler.setR(message.get(1).toFloat().toInt())
                    DataHandler.setRadValLeft(message.get(2).toFloat())
                    DataHandler.calculateE()

                    broadcastIntentMessageHandled!!.putExtra(EXTRA_MESSAGE_HANDLED_TYPE, TYPE_2)
                    LocalBroadcastManager.getInstance(context!!).sendBroadcast(broadcastIntentMessageHandled!!)

                    val radLevelChange = RadiationLevelChange(
                        message.get(1).toFloat().toInt(),
                        getTimeString()
                        )

                    RestHandler.postRadiationLevelChange(radLevelChange)

                }
            }

            "3" -> {
                // New hazmat status (0 = off, 1 = true)

                if(DataHandler.getIsClockedIn()) {

                    DataHandler.setPcFactor(message.get(1).toInt())
                    DataHandler.setRadValLeft(message.get(2).toFloat())
                    DataHandler.calculatePc()
                    DataHandler.calculateE()

                    broadcastIntentMessageHandled!!.putExtra(EXTRA_MESSAGE_HANDLED_TYPE, TYPE_3)
                    LocalBroadcastManager.getInstance(context!!).sendBroadcast(broadcastIntentMessageHandled!!)

                    val hazmatChange = HazmatChange(
                        DataHandler.getCurrentSessionId()!!,
                        message.get(1).toInt(),
                        getTimeString()
                    )

                    RestHandler.postHazmatChange(hazmatChange)

                }

            }

            "4" -> {
                // New room id {0,1,2}

                if(DataHandler.getIsClockedIn()) {

                    DataHandler.setRc(message.get(1).toFloat())
                    DataHandler.setRadValLeft(message.get(2).toFloat())
                    DataHandler.calculateE()

                    broadcastIntentMessageHandled!!.putExtra(EXTRA_MESSAGE_HANDLED_TYPE, TYPE_4)
                    LocalBroadcastManager.getInstance(context!!).sendBroadcast(broadcastIntentMessageHandled!!)

                    val roomChange = RoomChange(
                        DataHandler.getCurrentSessionId()!!,
                        message.get(1).toInt(),
                        getTimeString()
                    )

                    RestHandler.postRoomChange(roomChange)

                }

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
