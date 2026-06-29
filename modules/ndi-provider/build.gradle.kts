plugins {
    kotlin("jvm") version "2.3.21"
    `maven-publish`
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
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
        create<MavenPublication>("ndi-library-provider") {
            from(components["java"])
        }
    }
}