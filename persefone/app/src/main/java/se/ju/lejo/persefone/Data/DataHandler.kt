package se.ju.lejo.persefone.Data

object DataHandler {

    private var isClockedIn: Boolean = false
    private var radiationUnitExposurePerSecond: Float = 0.6f
    private var radiationUnitsUsed: Float = 0f

    fun setIsClockedIn(isClockedIn: Boolean) {
        this.isClockedIn = isClockedIn
    }

    fun getIsClockedIn(): Boolean {
        return this.isClockedIn
    }

    fun setE(e: Float) {
        this.radiationUnitExposurePerSecond = e
    }

    fun getE(): Float {
        return this.radiationUnitExposurePerSecond
    }

    fun incrementRadiationUnitsUsed() {
        this.radiationUnitsUsed += this.radiationUnitExposurePerSecond
    }

    fun setRadiationUnitsUsed(units: Float) {
        this.radiationUnitsUsed = units
    }

    fun getRadiationUnitsUsed(): Float {
        return this.radiationUnitsUsed
    }
}