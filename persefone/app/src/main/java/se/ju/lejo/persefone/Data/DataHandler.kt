package se.ju.lejo.persefone.Data

object DataHandler {

    private var isClockedIn: Boolean = false

    fun setIsClockedIn(isClockedIn: Boolean) {
        this.isClockedIn = isClockedIn
    }

    fun getIsClockedIn(): Boolean {
        return this.isClockedIn
    }
}