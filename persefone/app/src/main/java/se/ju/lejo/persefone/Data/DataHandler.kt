package se.ju.lejo.persefone.Data

object DataHandler {

    private var isClockedIn: Boolean = false
    private var currentSessionId: Int? = null

    private var E: Float? = null  // Human radiation exposure per second
    private var R: Int? = null    // Radiation output per second
    private var pcFactor: Int? = null // The dynamic factor of the dynamic addend in the formula of the protective coefficient
    private var pc: Int? = null   // Protective coefficient
    private var rc: Float? = null // Room coefficient
    private var ET: Float = 0f    // E (radiation exposure per second) * T (Time)

    fun setIsClockedIn(isClockedIn: Boolean) {
        this.isClockedIn = isClockedIn
    }

    fun getIsClockedIn(): Boolean {
        return this.isClockedIn
    }

    fun setCurrentSessionId(id: Int?) {
        this.currentSessionId = id
    }

    fun getCurrentSessionId(): Int? {
        return this.currentSessionId
    }

    fun calculateE() {
        this.E = (this.R!!.toFloat() * this.rc!!) / this.pc!!.toFloat()
    }

    fun setE(E: Float?) {
        this.E = E
    }

    fun getE(): Float? {
        return this.E
    }

    fun setR(R: Int?) {
        this.R = R
    }

    fun getR(): Int? {
        return this.R
    }

    fun calculatePc() {
        this.pc = 1 + 4 * this.pcFactor!!
    }

    fun setPcFactor(pcFactor: Int?) {
        this.pcFactor = pcFactor
    }

    fun setPc(pc: Int?) {
        this.pc = pc
    }

    fun getPc(): Int? {
        return this.pc
    }

    fun setRc(rc: Float?) {
        this.rc = rc
    }

    fun getRc(): Float? {
        return this.rc
    }

    fun incrementET() {
        this.ET += this.E!!
    }

    fun setET(ET: Float) {
        this.ET = ET
    }

    fun getET(): Float? {
        return this.ET
    }
}