import org.jetbrains.kotlin.gradle.utils.extendsFrom
import java.util.Properties

plugins {
    `maven-publish`
    kotlin("jvm") version "2.3.21"
}

kotlin {
    jvmToolchain(25)
}

configurations.register("windows-x86_64").extendsFrom(configurations.api)

val jarTask = file("ndi-info.properties").let { file ->
    val props = Properties()
    file.inputStream().use { props.load(it) }
    props
}.let { props ->
    val packageName = "bg.emanuil.ndi.discovory"
    val generatedClassName =
        "NDIProvider${props["platform"]}V${props["version"].toString().replace(".", "D")}"
    val classifier = "windows-x86_64"

    val generateClass = tasks.register("generateClassFile") {
        val outputDir = layout.buildDirectory.dir("generated/source/libraryProvider/main")
        outputs.dir(outputDir)

        doLast {
            val outputFile = File(outputDir.get().asFile, "${packageName.replace('.', '/')}/$generatedClassName.kt")
            outputFile.parentFile.mkdirs()

            // Generate the Kotlin class code matching your abstract constructor
            val code = """
            package $packageName

            import bg.emanuil.ndi.discovory.NdiProvider

            class $generatedClassName : NdiProvider(
                version = "${props["version"]}",
                platform = "$classifier",
                info = "${props["platform"]} ${props["date"]} ${props["version"]}",
                libFileName = "${file(props["path"].toString()).name}"
            )
        """.trimIndent()

            outputFile.writeText(code)
        }
    }
    val directory = layout.buildDirectory.dir("generated/resources/main")
    val generateSPI = tasks.register("generateSPI") {
        val file = directory.map { File(it.asFile,
            "META-INF/services/bg.emanuil.ndi.discovory.NDILinkedLibrary") }
        outputs.file(file)
        doLast {
            var actual = file.get()
            actual.parentFile.mkdirs()
            actual.writeText("$packageName.$generatedClassName\n")
        }
    }

    tasks.compileKotlin {
        dependsOn(generateClass)
    }

    tasks.processResources { dependsOn(generateSPI) }

    var jarTask = tasks.register<Jar>("ndiJar") {
        group = "build"
        version = props["version"].toString()
        from(props["path"])
        dependsOn(tasks.classes)
        dependsOn(tasks.processResources)
        from(layout.buildDirectory.dir("classes/kotlin/main"))
        from(layout.buildDirectory.dir("resources/main"))
        from(file("ndi-info.properties"))
        from(directory)

        manifest {
            attributes["Library-Path"] = "/${file(props["path"].toString()).name}"
            attributes["Library-Version"] = "${props["version"]}"
            attributes["Library-Platform"] = classifier
            attributes["Library-Info"] = "${props["platform"]} ${props["date"]} ${props["version"]}"
        }
    }

    project.artifacts.add(classifier, jarTask)

    jarTask
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Emanuil-Bulgaria/opensong-extensions")
            credentials {
                username = providers.gradleProperty("gpr.user").orNull  ?: System.getenv("GITHUB_ACTOR")
                password = providers.gradleProperty("gpr.key").orNull ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("ndi-library") {
            artifactId = "ndi-runtime-windows-x86_64"
            artifact(jarTask)
        }
    }
}

dependencies {
    api(project(":modules:ndi-provider"))
}


tasks.jar { enabled = false }

tasks.build {
    dependsOn(jarTask)
}