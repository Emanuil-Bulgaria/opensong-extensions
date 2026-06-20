plugins {
    kotlin("jvm") version "2.3.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.skiko:skiko-awt:0.148.2") // Use the latest version

    // You MUST include the runtime for the target operating system/architecture.
    // For local development on a 64-bit Windows/Linux/Mac machine:
    runtimeOnly("org.jetbrains.skiko:skiko-awt-runtime-windows-x64:0.148.2")
    runtimeOnly("org.jetbrains.skiko:skiko-awt-runtime-linux-x64:0.148.2")
    runtimeOnly("org.jetbrains.skiko:skiko-awt-runtime-macos-x64:0.148.2")
    runtimeOnly("org.jetbrains.skiko:skiko-awt-runtime-macos-arm64:0.148.2")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}