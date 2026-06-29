package bg.emanuil.ndi.discovory

import java.io.File

abstract class NdiProvider(override val info: String,
                  override val platform: String,
                  override val version: String,
                  val libFileName: String) : NDILinkedLibrary {

    override val linkedLibrary: File by lazy {
        var resource = ClassLoader
            .getSystemClassLoader()
            .getResourceAsStream(libFileName)
        var outFile = File(System.getProperty("java.io.tmpdir"), libFileName)
        println(System.getProperty("os.name"))

        if(resource == null) {
            throw IllegalStateException("$libFileName not found in classpath")
        }

        resource
            .use { reader ->
                outFile
                    .outputStream()
                    .use { output -> output.write(reader.readBytes()) }
            }
        outFile
    }
}