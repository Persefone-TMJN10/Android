package se.ju.lejo.persefone.GottOchBlandat

import se.ju.lejo.persefone.Data.DataHandler

class RadiationTracker {

    companion object {
        val RADIATION_LIMIT: Float = 500000f
    }

    fun calculateTimeLeft(): Long {
        //val radiationLeft: Float = RADIATION_LIMIT - DataHandler.getET()!!

        val radiationLeft = DataHandler.getRadValLeft()

        return ((radiationLeft / DataHandler.getE()!!) * 1000f).toLong()
    }
}