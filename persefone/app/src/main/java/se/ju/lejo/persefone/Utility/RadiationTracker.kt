package se.ju.lejo.persefone.Utility

import se.ju.lejo.persefone.Data.DataHandler

class RadiationTracker {

    fun calculateTimeLeft(): Long {

        val radiationLeft = DataHandler.getRadValLeft()

        return ((radiationLeft / DataHandler.getE()!!) * 1000f).toLong()
    }
}