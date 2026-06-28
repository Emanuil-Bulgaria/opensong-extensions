import java.util.Properties

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

val jarTasks = file("ndi-info").listFiles().map {
    val props = Properties()
    it.inputStream().use { props.load(it) }
    val classifier = it.name.replace(".properties", "")
    return@map tasks.register<Jar>("jar-${classifier}") {
        archiveClassifier.set(classifier)
        group = "build"
        version = props["version"].toString()
        from(props["path"])
    }
}

publishing {
    publications {
        create<MavenPublication>("ndi-library") {
            jarTasks.forEach { task -> artifact(task) }
        }
    }
}

tasks.jar { enabled = false }

tasks.build {
    jarTasks.forEach { task -> dependsOn(task) }
}