package net.guneyilmaz0.rcon.packet

data class Packet(
    val requestId: Int,
    val type: Int,
    val payload: String = ""
) {
    val isValid: Boolean
        get() = requestId != -1
}
