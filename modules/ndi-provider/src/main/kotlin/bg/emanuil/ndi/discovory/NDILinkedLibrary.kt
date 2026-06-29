package bg.emanuil.ndi.discovory

import java.io.File

interface NDILinkedLibrary {

    val linkedLibrary: File
    val version: String
    val info: String
    val platform: String
}