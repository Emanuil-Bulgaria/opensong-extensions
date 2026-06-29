plugins {
    kotlin("jvm") version "2.3.21"
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.skiko:skiko-awt:0.148.2") // Use the latest version
    implementation("bg.emanuil:ndi-library:6.3.2.0:windows-x86_64")
    implementation("bg.emanuil:ndi-provider:1.0-SNAPSHOT")
//    implementation(project(":distributions:ndi-library", configuration = "windows-x86_64"))

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