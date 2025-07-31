package net.guneyilmaz0.rcon.packet

import java.nio.ByteBuffer
import java.nio.charset.Charset

class PacketCodec(private val charset: Charset) {

    fun encode(packet: Packet, destination: ByteBuffer) {
        destination.putInt(packet.requestId)
        destination.putInt(packet.type)
        destination.put(charset.encode(packet.payload))
        destination.put(0x00)
        destination.put(0x00)
    }

    fun decode(source: ByteBuffer, length: Int): Packet {
        val requestId = source.int
        val packetType = source.int

        val limit = source.limit()
        source.limit(source.position() + length - 10)
        val payload = charset.decode(source).toString()
        source.limit(limit)

        source.get()
        source.get()

        return Packet(requestId, packetType, payload)
    }
}
