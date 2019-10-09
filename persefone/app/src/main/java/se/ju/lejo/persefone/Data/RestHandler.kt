package se.ju.lejo.persefone.Data

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.json.responseJson
import se.ju.lejo.persefone.Data.Resources.HazmatChange
import se.ju.lejo.persefone.Data.Resources.RadiationLevelChange
import se.ju.lejo.persefone.Data.Resources.RoomChange
import se.ju.lejo.persefone.Data.Resources.StartEnvironment
import se.ju.lejo.persefone.Models.HistoryListItem

object RestHandler {
    private val APIAddress = "http://3.122.218.59/"
    private val sessionEndpoint = APIAddress + "session"
    private val startEnvironmentEndpoint = APIAddress + "start-environment"
    private val radiationLevelChangeEndpoint = APIAddress + "radiation-level-change"
    private val hazmatChangeEndpoint = APIAddress + "hazmat-change"
    private val roomChangeEndpoint = APIAddress + "room-change"

    fun getSessionForSpecificRFID(RFID: String?, completion: (success: Boolean) -> Unit) {

        var getSessionForSpecificRFIDEndpoint = sessionEndpoint

        if(RFID != null) {
            getSessionForSpecificRFIDEndpoint += "?tagId=$RFID"
        }

        Fuel.get(getSessionForSpecificRFIDEndpoint)
            .timeout(10000)
            .responseJson { _, response, result ->
                println(response.statusCode)
                println(response.responseMessage)

                if (response.statusCode == 200) {
                    val results = result.get().obj()
                    val jsonArray = results.getJSONArray("payload")
                    val historyListForRFID = ArrayList<HistoryListItem>()
                    for (i in 0 until (jsonArray.length())) {
                        val item = jsonArray.getJSONObject(i)
                        val tempListItem = HistoryListItem(item["inTime"].toString(), item["outTime"].toString(), item["totalExposure"].toString())
                        historyListForRFID.add(tempListItem)
                    }
                    DataHandler.setHistoryListForRFID(historyListForRFID)
                    completion(true)
                }
                completion(false)
            }
    }

    fun postSession(tagId: String, inTime: String, completion: (success: Boolean) -> Unit) {

        println(tagId + " " + inTime)

        Fuel.post(sessionEndpoint, listOf("tagId" to tagId,"inTime" to inTime))
            .timeout(10000)
            .responseJson {
                    _, response, result ->
                println(response.statusCode)
                println(response.responseMessage)

                if(response.statusCode == 200) {
                    val sessionId = result.get().obj().get("insertId")
                    DataHandler.setCurrentSessionId(sessionId.toString().toInt())
                    completion(true)
                }

                completion(false)
            }
    }

    fun putSession(sessionId: Int, outTime: String, totalExposure: Long) {

        Fuel.put(sessionEndpoint, listOf("id" to sessionId, "outTime" to outTime, "totalExposure" to totalExposure))
            .responseJson {
                    request, response, result ->
                println(response.statusCode)
                println(response.responseMessage)
            }
    }

    fun postStartEnvironment(data: StartEnvironment) {


        Fuel.post(
            startEnvironmentEndpoint,
            listOf(
                "sessionId" to data.sessionId,
                "radiationLevel" to data.radLevel,
                "hazmatStatus" to data.hazmatStatus,
                "roomId" to data.roomId
            )
        )
            .timeout(10000)
            .responseJson {
                    request, response, result ->
                println(response.statusCode)
                println(response.responseMessage)
            }
    }

    fun postRadiationLevelChange(data: RadiationLevelChange) {


        Fuel.post(
            radiationLevelChangeEndpoint,
            listOf(
                "level" to data.level,
                "time" to data.time
            )
        )
            .timeout(10000)
            .responseJson {
                    request, response, result ->
                println(response.statusCode)
                println(response.responseMessage)
            }
    }

    fun postHazmatChange(data: HazmatChange) {


        Fuel.post(
            hazmatChangeEndpoint,
            listOf(
                "sessionId" to data.sessionId,
                "status" to data.status,
                "time" to data.time
            )
        )
            .timeout(10000)
            .responseJson {
                    request, response, result ->
                println(response.statusCode)
                println(response.responseMessage)
            }
    }

    fun postRoomChange(data: RoomChange) {


        Fuel.post(
            roomChangeEndpoint,
            listOf(
                "sessionId" to data.sessionId,
                "roomId" to data.roomId,
                "time" to data.time
            )
        )
            .timeout(10000)
            .responseJson {
                    request, response, result ->
                println(response.statusCode)
                println(response.responseMessage)
            }
    }

}