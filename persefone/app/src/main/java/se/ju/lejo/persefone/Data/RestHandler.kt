package se.ju.lejo.persefone

import android.os.AsyncTask
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

class RestHandler: AsyncTask<Void, Void, String>() {

    fun sendGet() {

        val httpAsync = "http://3.122.218.59/session"
            .httpGet()
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        println(ex)
                    }
                    is Result.Success -> {
                        val data = result.get()
                        println(data)
                    }
                }
            }

        httpAsync.join()
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