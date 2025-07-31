package net.guneyilmaz0.rcon

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFails

class RCONTest {

    private val hostname = "localhost"
    private val port = 19132
    private val password = "password"

    @Test
    fun testConnectionAndCommand() {
        RCON.open(hostname, port).use { rcon ->
            rcon.tryAuthenticate(password)
            val response = rcon.sendCommand("list")
            println("Response: $response")
            assertTrue(response.isNotEmpty(), "Response should not be empty")
        }
    }

    @Test
    fun testAuthenticationFailure() {
        assertFails {
            RCON.open(hostname, port).use { rcon ->
                rcon.tryAuthenticate("wrong_password")
            }
        }
    }
}
