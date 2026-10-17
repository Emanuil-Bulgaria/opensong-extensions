import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "2.3.21"
    kotlin("plugin.serialization") version "2.4.20"

    kotlin("plugin.compose") version "2.3.21"
    id("org.jetbrains.compose") version "1.9.3"
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")

    implementation(compose.desktop.currentOs)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.compose)
    implementation(libs.lifecycle.viewmodel.compose)

    implementation("io.github.pdvrieze.xmlutil:serialization:1.0.2")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.0.2")

//    implementation("org.jetbrains.skiko:skiko-awt:0.148.2") // Use the latest version
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
//    runtimeOnly("org.jetbrains.skiko:skiko-awt-runtime-windows-x64:0.148.2")
//    runtimeOnly("org.jetbrains.skiko:skiko-awt-runtime-linux-x64:0.148.2")
//    runtimeOnly("org.jetbrains.skiko:skiko-awt-runtime-macos-x64:0.148.2")
//    runtimeOnly("org.jetbrains.skiko:skiko-awt-runtime-macos-arm64:0.148.2")
}

kotlin {
    jvmToolchain(25)
}

compose.desktop {
    application {

        mainClass = "bg.emanuil.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.AppImage)
            packageName = "MyDesktopApp"
            packageVersion = "1.0.0"

            includeAllModules = true
        }
    }
}

tasks.test {
    useJUnitPlatform()
}