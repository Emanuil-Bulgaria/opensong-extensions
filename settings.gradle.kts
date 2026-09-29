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

        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Emanuil-Bulgaria/opensong-extensions")
            credentials {
                username = providers.gradleProperty("gpr.user").orNull  ?: System.getenv("GITHUB_ACTOR")
                password = providers.gradleProperty("gpr.key").orNull ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}