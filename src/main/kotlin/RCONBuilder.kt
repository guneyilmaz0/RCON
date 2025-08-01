package net.guneyilmaz0.rcon

import net.guneyilmaz0.rcon.packet.PacketCodec
import java.nio.channels.ByteChannel
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class RCONBuilder {

    private var channel: ByteChannel? = null
    private var readBufferCapacity: Int = 4110
    private var writeBufferCapacity: Int = 1460
    private var charset: Charset = StandardCharsets.UTF_8

    fun withChannel(channel: ByteChannel) = apply { this.channel = channel }

    fun withReadBufferCapacity(capacity: Int) = apply { this.readBufferCapacity = capacity }

    fun withWriteBufferCapacity(capacity: Int) = apply { this.writeBufferCapacity = capacity }

    fun withCharset(charset: Charset) = apply { this.charset = charset }

    fun build(): RCON {
        val chan = requireNotNull(channel) { "Channel must be set before building RCON" }
        return RCON(chan, readBufferCapacity, writeBufferCapacity, PacketCodec(charset))
    }
}