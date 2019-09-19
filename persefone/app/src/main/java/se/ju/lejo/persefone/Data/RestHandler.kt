package se.ju.lejo.persefone.Data

import android.os.AsyncTask
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import org.json.JSONArray
import org.json.JSONObject
import se.ju.lejo.persefone.Models.Session

class RestHandler: AsyncTask<Void, Void, String>() {

    private val _API = "http://3.122.218.59/"
    private val _clockInAddress = _API + "session"

    fun sendGetRequest() {
        _clockInAddress.httpGet().responseJson { _, _, result ->
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

    fun sendClockInPostRequest(inTime: String) {

        Fuel.post(_clockInAddress, listOf("inTime" to inTime))
            .timeout(10000)
            .responseJson {
                request, response, result ->
                println(response.statusCode)
                println(response.responseMessage)
            }
    }

    private fun handleJsonSessionsArray(jsonString: String): ArrayList<Session>? {

        val jsonArray = JSONArray(jsonString)

        val list = ArrayList<Session>()

        var x = 0

        while (x < jsonArray.length()) {

            val task = Session(jsonArray[x].toString())

            list.add(task)

            x++

        }

        return list

    }

    override fun doInBackground(vararg params: Void?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPreExecute() {
        super.onPreExecute()
        // ...
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        // ....
    }
}