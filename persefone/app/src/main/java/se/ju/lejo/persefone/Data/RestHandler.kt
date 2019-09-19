package se.ju.lejo.persefone.Data

import android.os.AsyncTask
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import org.json.JSONObject

class RestHandler: AsyncTask<Void, Void, String>() {

    fun sendGetRequest(address: String) {
        val httpAsync = address
            .httpGet()
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val exception = result.getException()
                        println(exception)
                    }
                    is Result.Success -> {
                        val data = result.get()
                        println(data)
                    }
                }
            }
        httpAsync.join()
    }

    fun sendPostRequest(address: String, inTime: String, outTime: String) {
        val bodyJson = """
            {"inTime" : "2019-12-16 14:13:37",
                "outTime" : "2019-12-16 16:13:32"
            }
        """

        Fuel.post(address, listOf("inTime" to "2019-12-16 14:13:37"))
            .timeout(10000)
            .responseJson {
                request, response, result ->
                println(response.statusCode)
                println(response.responseMessage)
            }
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