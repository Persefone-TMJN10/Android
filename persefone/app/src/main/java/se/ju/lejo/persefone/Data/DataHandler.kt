package se.ju.lejo.persefone.Data

object DataHandler {

    private var _isClockedIn: Boolean = false
    var _E: Long = 30

    fun setIsClockedIn(isClockedIn: Boolean) {
        this._isClockedIn = isClockedIn
    }

    fun getIsClockedIn(): Boolean {
        return this._isClockedIn
    }

    fun setE(e: Long) {
        this._E = e
    }

    fun getE(): Long {
        return this._E
    }
}