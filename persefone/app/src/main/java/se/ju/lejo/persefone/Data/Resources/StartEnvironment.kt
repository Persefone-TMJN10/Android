package se.ju.lejo.persefone.Data.Resources

data class StartEnvironment(
    var sessionId: Int,
    var radLevel: Int,
    var hazmatStatus: Int,
    var roomId: Int
) {}