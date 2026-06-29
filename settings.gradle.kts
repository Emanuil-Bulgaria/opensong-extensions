import org.gradle.kotlin.dsl.maven

rootProject.name = "ndi-demo"

include("modules:text-presenter")
project(":modules:text-presenter").projectDir = File("modules/text-presenter")

include("modules:ndi-provider")
project(":modules:ndi-provider").projectDir = File("modules/ndi-provider")

include("distributions:ndi-library")
project(":distributions:ndi-library").projectDir = File("distributions/ndi-library")


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