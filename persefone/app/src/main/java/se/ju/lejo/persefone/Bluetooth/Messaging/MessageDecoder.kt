package com.root.bluetoothtester.Bluetooth.Messaging

class MessageDecoder {

    private var supportedSizes: ArrayList<Int>? = null
    private var splitChar = ","

    constructor() {

        supportedSizes = ArrayList()
        supportedSizes!!.add(2)
        supportedSizes!!.add(5)

    }

    fun decodeMessage(message: String): List<String>? {

        var splitMessage = message.split(splitChar)

        if(supportedSizes!!.contains(splitMessage.size)) {

            return splitMessage

        }

        return null

    }

}