plugins {
    `maven-publish`
    kotlin("jvm") version "2.3.21"
}

kotlin {
    jvmToolchain(25)

}

tasks.register<JavaExec>("getVersion") {
    group = "application"
    dependsOn(tasks.named("classes"))

    classpath = sourceSets.named("main").get().runtimeClasspath

    mainClass.set("org.example.MainKt")

    args("C:\\Program Files\\NDI\\NDI 6 Runtime\\v6")
}

var windowsJar = tasks.register<Jar>("windowsJar") {
    archiveClassifier.set("windows-x86_64")
    group = "build"
    from("C:\\Program Files\\NDI\\NDI 6 Runtime\\v6\\Processing.NDI.Lib.x64.dll")
}

publishing {
    publications {
        create<MavenPublication>("ndi-library") {
            artifact(windowsJar)
        }
    }
}

tasks.jar { enabled = false }

tasks.build {
    dependsOn(windowsJar)
}