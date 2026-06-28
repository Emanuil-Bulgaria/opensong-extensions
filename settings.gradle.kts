rootProject.name = "ndi-demo"

include("modules:text-presenter")
project(":modules:text-presenter").projectDir = File("modules/text-presenter")

include("modules:ndi-provider")
project(":modules:ndi-provider").projectDir = File("modules/ndi-provider")

include("distributions:ndi-library")
project(":distributions:ndi-library").projectDir = File("distributions/ndi-library")
