package se.ju.lejo.persefone.GottOchBlandat

import se.ju.lejo.persefone.Data.DataHandler

class RadiationTracker {

    companion object {
        val RADIATION_LIMIT: Float = 500000f
    }

    var _radiationUnitsUsed: Float = 0f

    fun calculateTimeLeft(): Long {
        val radiationLeft: Float = RADIATION_LIMIT - _radiationUnitsUsed

        return ((radiationLeft / DataHandler.getE()) * 1000f).toLong()
    }
}