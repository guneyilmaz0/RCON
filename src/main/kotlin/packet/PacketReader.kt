package net.guneyilmaz0.rcon.packet

import java.io.EOFException
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class PacketReader(
    private val source: Source,
    bufferCapacity: Int,
    private val codec: PacketCodec
) {
    private val buffer: ByteBuffer = ByteBuffer.allocate(bufferCapacity).order(ByteOrder.LITTLE_ENDIAN)

    @Throws(IOException::class)
    fun read(): Packet {
        readUntilAvailable(Int.SIZE_BYTES)
        buffer.flip()
        val length = buffer.int
        buffer.compact()

        readUntilAvailable(length)
        buffer.flip()
        val packet = codec.decode(buffer, length)
        buffer.compact()
        return packet
    }

    @Throws(IOException::class)
    private fun readUntilAvailable(bytesAvailable: Int) {
        while (buffer.position() < bytesAvailable) {
            if (source.read(buffer) == -1) throw EOFException()
        }
    }

    fun interface Source {
        @Throws(IOException::class)
        fun read(destination: ByteBuffer): Int
    }
}
