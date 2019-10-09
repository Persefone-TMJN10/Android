package com.root.bluetoothtester.Bluetooth.Messaging

import android.content.Context
import com.root.bluetoothtester.Bluetooth.Streaming.ServiceController

class MessageReader {

    // MESSAGES
    private var msgList: ArrayList<String>? = null
    private var msgDecoder: MessageDecoder? = null
    private var msgHandler: MessageHandler? = null

    // READING
    private var msgReader: Thread? = null

    constructor() {

        msgList = ArrayList()
        msgDecoder = MessageDecoder()

        msgReader = Thread() {

            kotlin.run {

                while(!msgReader!!.isInterrupted) {

                    if(ServiceController.getService() != null) {

                        var msg = ServiceController.getService()!!.read()

                        if(msg != null) {

                            var msgList = msgDecoder!!.decodeMessage(msg)

                            if(msgHandler != null && msgList != null) {

                                msgHandler!!.handleMessage(msgList!!)

                            }

                        }

                    }

                }

            }

        }

    }

    fun start() {

        msgReader!!.start()

    }

    fun constructMessageHandler(context: Context) {

        msgHandler = MessageHandler(context)

    }

}