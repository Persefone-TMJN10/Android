package se.ju.lejo.persefone.Data

object DataHandler {

    private var _isClockedIn: Boolean = false

    fun setIsClockedIn(isClockedIn: Boolean) {
        this._isClockedIn = isClockedIn
    }

    fun getIsClockedIn(): Boolean {
        return this._isClockedIn
    }
}