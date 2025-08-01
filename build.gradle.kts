plugins {
    kotlin("jvm") version "2.1.21"
    application
}

group = "net.guneyilmaz0.rcon"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("net.guneyilmaz0.rcon.MainKt")
}