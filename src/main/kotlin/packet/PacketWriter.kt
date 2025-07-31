package net.guneyilmaz0.rcon.packet

import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class PacketWriter(
    private val destination: Destination,
    bufferCapacity: Int,
    private val codec: PacketCodec
) {
    private val buffer: ByteBuffer = ByteBuffer.allocate(bufferCapacity).order(ByteOrder.LITTLE_ENDIAN)

    @Throws(IOException::class)
    fun write(packet: Packet): Int {
        if (packet.payload.length > 1446) {
            throw IllegalArgumentException("Paket içeriği çok büyük")
        }

        buffer.clear()
        buffer.position(Int.SIZE_BYTES)
        codec.encode(packet, buffer)
        buffer.putInt(0, buffer.position() - Int.SIZE_BYTES)
        buffer.flip()
        return destination.write(buffer)
    }

    fun interface Destination {
        @Throws(IOException::class)
        fun write(source: ByteBuffer): Int
    }
}
