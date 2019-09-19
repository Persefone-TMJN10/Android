package se.ju.lejo.persefone.Models

import org.json.JSONObject

class Session(
    var inTime: String = "",
    var outTime: String = ""
) {

    constructor(jsonString: String): this() {
        val jsonObject = JSONObject(jsonString)
        if (!jsonObject.has("null")) {
            this.inTime = jsonObject.getString("inTime")
            this.outTime = jsonObject.getString("outTime")
        }
    }
}