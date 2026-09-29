plugins {
    kotlin("jvm") version "2.3.21"
    kotlin("plugin.serialization") version "2.4.20"
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")

    implementation("org.jetbrains.skiko:skiko-awt:0.148.2") // Use the latest version
    implementation("bg.emanuil:ndi-runtime-linux-x86_64:6.3.2.0")
    implementation("bg.emanuil:ndi-runtime-windows-x86_64:6.3.2.0")
    implementation(project(":modules:ndi-provider"))
    implementation("com.fasterxml.woodstox:woodstox-core:7.2.1")

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)

    implementation(libs.bundles.logging)

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")


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