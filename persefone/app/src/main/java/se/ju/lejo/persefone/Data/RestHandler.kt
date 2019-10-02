package se.ju.lejo.persefone.Data

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import se.ju.lejo.persefone.Data.Resources.HazmatChange
import se.ju.lejo.persefone.Data.Resources.StartEnvironment
import se.ju.lejo.persefone.Models.Session

object RestHandler {
    private val APIAddress = "http://3.122.218.59/"
    private val sessionEndpoint = APIAddress + "session"
    private val startEnvironmentEndpoint = APIAddress + "start-environment"
    private val hazmatChangeEndpoint = APIAddress + "hazmat-change"

    fun sendGetRequest() {
        sessionEndpoint.httpGet().responseJson { _, _, result ->
            var results = result.get().obj()
            var jsonArray = results.getJSONArray("payload")
            var sessionList = ArrayList<Session>()

            for (i in 0..(jsonArray.length() - 1)) {
                val item = jsonArray.getJSONObject(i)
                val currentItem = Session(item.toString())
                sessionList.add(currentItem)
            }
        }
    }

    fun postSession(tagId: String, inTime: String) {

        println(tagId + " " + inTime)

        Fuel.post(sessionEndpoint, listOf("tagId" to tagId,"inTime" to inTime))
            .timeout(10000)
            .responseJson {
                    request, response, result ->
                println(response.statusCode)
                println(response.responseMessage)
            }
    }

    fun putSession(tagId: String, outTime: String) {

        Fuel.put(sessionEndpoint, listOf("tagId" to tagId, "outTime" to outTime))
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

}