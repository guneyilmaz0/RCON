# Kotlin Rcon Client

A Kotlin library for communicating with Minecraft servers via RCON protocol with full UTF-8 support, including Turkish characters like "ı".

---

## Adding the Dependency via JitPack

To use this library in your Kotlin/Java project, add the JitPack repository and the dependency to your `build.gradle.kts` file:

```gradle
repositories {
    mavenCentral()
    // Add repository if not published to Maven Central
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.guneyilmaz0:RCON:1.0'
}
```

## Quick Start

- Kotlin
```kotlin

fun main() {
    val hostname = "localhost"
    val port = 19132
    val password = "rconpassword"

    try {
        Rcon.open(hostname, port).use { rcon ->
            rcon.tryAuthenticate(password)
            val response = rcon.sendCommand("list")
            println("Response: $response")
        }
    } catch (ex: Exception) {
        println("Error: ${ex.message}")
        ex.printStackTrace()
    }
}
```
