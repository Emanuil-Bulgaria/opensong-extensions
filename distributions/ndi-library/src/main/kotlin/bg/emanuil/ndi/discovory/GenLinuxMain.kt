package bg.emanuil.ndi.discovory

import java.io.File

fun main() {
    val info = getLibraryInfo(File("build/lib/libndi.so").absoluteFile)

    println(info)

    writeProperties(info)
}