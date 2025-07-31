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
