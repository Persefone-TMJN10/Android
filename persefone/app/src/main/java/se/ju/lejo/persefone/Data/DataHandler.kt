package se.ju.lejo.persefone.Data

object DataHandler {

    private var _isClockedIn: Boolean = false
    var _E: Float = 30.0f

    fun setIsClockedIn(isClockedIn: Boolean) {
        this._isClockedIn = isClockedIn
    }

    fun getIsClockedIn(): Boolean {
        return this._isClockedIn
    }

    fun setE(e: Float) {
        this._E = e
    }

    fun getE(): Float {
        return this._E
    }
}