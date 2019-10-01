package se.ju.lejo.persefone.Data

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import se.ju.lejo.persefone.Models.Session

object RestHandler {
    private val APIAddress = "http://3.122.218.59/"
    private val clockInAddress = APIAddress + "session"

    fun sendGetRequest() {
        clockInAddress.httpGet().responseJson { _, _, result ->
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

    fun sendClockInPostRequest(tagId: String, inTime: String) {

        println(tagId + " " + inTime)

        Fuel.post(clockInAddress, listOf("tagId" to tagId,"inTime" to inTime))
            .timeout(10000)
            .responseJson {
                    request, response, result ->
                println(response.statusCode)
                println(response.responseMessage)
            }
    }

    fun requestUpdateSession(tagId: String, outTime: String) {

        Fuel.put(clockInAddress, listOf("tagId" to tagId, "outTime" to outTime))
            .responseJson {
                    request, response, result ->
                println(response.statusCode)
                println(response.responseMessage)
            }
    }
}