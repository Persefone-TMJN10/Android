package se.ju.lejo.persefone.GottOchBlandat

import se.ju.lejo.persefone.Data.DataHandler

class RadiationTracker {

    companion object {
        val RADIATION_LIMIT: Long = 500000
    }

    var _radiationUnitsUsed: Long = 0

    fun calculateTimeLeft(): Long {
        val radiationLeft: Long = RADIATION_LIMIT - _radiationUnitsUsed

        return (radiationLeft / DataHandler.getE()) * 1000
    }
}