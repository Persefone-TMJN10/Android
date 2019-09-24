package se.ju.lejo.persefone.GottOchBlandat

import se.ju.lejo.persefone.Data.DataHandler
import se.ju.lejo.persefone.Data.RestHandler
import java.text.SimpleDateFormat
import java.util.*

object Controller {

    fun translateDataFromService(dataString: String) {
        var dataList: List<String> = arrayListOf()

        //TODO: was this an ok way of doing this?
        if(dataString.split(";").size > 1) {
            dataList = dataString.substring(0, dataString.length-1).split(",")
        }

        //TODO: refactor name?
        actBasedOnProtocol(dataList)
    }

    fun actBasedOnProtocol(dataList: List<String>) {

        when (dataList[0]) {

            "0" -> {

                when (dataList[2]) {
                    "0" -> {
                        RestHandler.requestUpdateSession(dataList[1], getTimeString())
                        DataHandler.setIsClockedIn(false)

                        //TODO: Broadcast to TimerFragment
                    }
                    "1" -> {
                        RestHandler.sendClockInPostRequest(dataList[1], getTimeString())
                        DataHandler.setIsClockedIn(true)

                        //TODO: Broadcast to TimerFragment
                    }
                }
            }

            "1" -> {
                // changes to E
                DataHandler.setE(dataList[2].toLong())

                //TODO: Broadcast to TimerFragment
            }
        }
    }

    fun getTimeString(): String {
        val calendar: Calendar = Calendar.getInstance()
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormatter.format(calendar.time)
    }
}