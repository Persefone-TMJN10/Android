package se.ju.lejo.persefone.Models

import org.json.JSONObject

class Session(
    private var tagId: String = "",
    private var inTime: String = "",
    private var outTime: String = "",
    private var radiationLevel: String = ""
) {

    constructor(jsonString: String): this() {
        val jsonObject = JSONObject(jsonString)
        if (!jsonObject.has("null")) {
            this.tagId = jsonObject.getString("tagId")
            this.inTime = jsonObject.getString("inTime")
            this.outTime = jsonObject.getString("outTime")
            this.radiationLevel = jsonObject.getLong("totalExposure").toString()
        }
    }
}