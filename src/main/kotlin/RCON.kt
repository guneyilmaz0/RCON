package net.guneyilmaz0.rcon

import net.guneyilmaz0.rcon.packet.*
import java.io.Closeable
import java.io.IOException
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.channels.ByteChannel
import java.nio.channels.SocketChannel

class RCON(
    private val channel: ByteChannel,
    readBufferCapacity: Int,
    writeBufferCapacity: Int,
    codec: PacketCodec
) : Closeable {

    private val reader = PacketReader(channel::read, readBufferCapacity, codec)
    private val writer = PacketWriter(channel::write, writeBufferCapacity, codec)

    @Volatile
    private var requestCounter = 0

    companion object {
        @Throws(IOException::class)
        fun open(remote: SocketAddress): RCON {
            return RCONBuilder().withChannel(SocketChannel.open(remote)).build()
        }

        @Throws(IOException::class)
        fun open(hostname: String, port: Int): RCON {
            return open(InetSocketAddress(hostname, port))
        }

        fun newBuilder(): RCONBuilder = RCONBuilder()
    }

    @Throws(IOException::class)
    fun authenticate(password: String): Boolean {
        var response: Packet
        synchronized(this) {
            response = writeAndRead(PacketType.SERVERDATA_AUTH, password)

            if (response.type == PacketType.SERVERDATA_RESPONSE_VALUE) {
                response = read(response.requestId)
            }
        }
        if (response.type != PacketType.SERVERDATA_AUTH_RESPONSE) {
            throw IOException("Wrong authentication response type: ${response.type}")
        }
        return response.isValid
    }

    @Throws(IOException::class)
    fun tryAuthenticate(password: String) {
        if (!authenticate(password)) {
            throw IOException("Authentication failed")
        }
    }

    @Throws(IOException::class)
    fun sendCommand(command: String): String {
        val response = writeAndRead(PacketType.SERVERDATA_EXECCOMMAND, command)

        if (response.type != PacketType.SERVERDATA_RESPONSE_VALUE) {
            throw IOException("Wrong command response type: ${response.type}")
        }
        if (!response.isValid) {
            throw IOException("Invalid command response: ${response.payload}")
        }
        return response.payload
    }

    @Synchronized
    @Throws(IOException::class)
    private fun writeAndRead(packetType: Int, payload: String): Packet {
        val requestId = requestCounter++
        writer.write(Packet(requestId, packetType, payload))
        return read(requestId)
    }

    @Synchronized
    @Throws(IOException::class)
    private fun read(expectedRequestId: Int): Packet {
        val response = reader.read()
        if (response.isValid && response.requestId != expectedRequestId) {
            throw IOException("Unexpected response request ID: ${response.requestId}, expected: $expectedRequestId")
        }
        return response
    }

    @Throws(IOException::class)
    override fun close() {
        channel.close()
    }
}