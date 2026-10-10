import org.gradle.kotlin.dsl.maven

rootProject.name = "ndi-demo"

include("modules:text-presenter")
project(":modules:text-presenter").projectDir = File("modules/text-presenter")

include("modules:ndi-provider")
project(":modules:ndi-provider").projectDir = File("modules/ndi-provider")

include("distributions:ndi-runtime-linux")
project(":distributions:ndi-runtime-linux").projectDir = File("distributions/ndi-runtime-linux")

include("distributions:ndi-runtime-windows")
project(":distributions:ndi-runtime-windows").projectDir = File("distributions/ndi-runtime-windows")

file(".env").takeIf { it.exists() }?.readLines()?.forEach { line ->
    val trimmed = line.trim()
    if (trimmed.isNotEmpty() && !trimmed.startsWith("#")) {
        val pos = trimmed.indexOf("=")
        if (pos != -1) {
            val key = trimmed.substring(0, pos).trim()
            val value = trimmed.substring(pos + 1).trim()

            if (System.getenv(key) == null) {
                System.setProperty(key, value)
            }
        }
    }
}

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()

        maven {
            println(System.getenv("GITHUB_ACTOR"))
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Emanuil-Bulgaria/opensong-extensions")
            credentials {
                username = providers.gradleProperty("gpr.user").orNull ?: System.getProperty("GITHUB_ACTOR") ?: System.getenv("GITHUB_ACTOR")
                password = providers.gradleProperty("gpr.key").orNull ?: System.getProperty("GITHUB_TOKEN") ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}