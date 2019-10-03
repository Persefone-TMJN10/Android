package se.ju.lejo.persefone.Data

import se.ju.lejo.persefone.Models.HistoryListItem

object DataHandler {

    private var isClockedIn: Boolean = false
    private var currentSessionId: Int? = null

    private var E: Float? = null  // Human radiation exposure per second
    private var R: Int? = null    // Radiation output per second
    private var pcFactor: Int? = null // The dynamic factor of the dynamic addend in the formula of the protective coefficient
    private var pc: Int? = null   // Protective coefficient
    private var rc: Float? = null // Room coefficient
    private var ET: Float = 0f    // E (radiation exposure per second) * T (Time)
    private var rfId: String = ""
    private lateinit var historyListForRFID: ArrayList<HistoryListItem>

    fun getHistoryListForRFID(): ArrayList<HistoryListItem> {
        return this.historyListForRFID
    }

    fun setHistoryListForRFID(arrayList: ArrayList<HistoryListItem>) {
        this.historyListForRFID = arrayList
    }

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

        when(rc) {
            0f -> this.rc = 0.1f
            1f -> this.rc = 0.5f
            2f -> this.rc = 1.6f
        }
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

    fun setRfId(rfId: String) {
        this.rfId = rfId
    }

    fun getRfId(): String {
        return this.rfId
    }
}