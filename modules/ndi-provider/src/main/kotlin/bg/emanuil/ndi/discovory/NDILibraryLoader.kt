package bg.emanuil.ndi.discovory

import java.util.ServiceLoader

object NDILibraryLoader {

    val libraries: Map<NDIPlatform, List<NDILinkedLibrary>> by lazy {
        ServiceLoader.load(
            NDILinkedLibrary::class.java,
            ClassLoader.getSystemClassLoader()
        )
        .groupBy { findPlatformByCode(it) }
    }

    private fun findPlatformByCode(library: NDILinkedLibrary): NDIPlatform {
        return NDIPlatform.entries.find { library.platform == it.code } ?:
        throw IllegalArgumentException("Unknown library platform: $library")
    }

    fun getCurrentNDIPlatform(): NDIPlatform {
        val os = System.getProperty("os.name")
        val arch = System.getProperty("os.arch")
        if(os.contains("Windows") && arch == "amd64") {
            return NDIPlatform.WINDOWS_X86_64
        }

        else throw IllegalStateException("Unknown platform ($os, $arch) is not supported")
    }

    fun getForCurrentNDIPlatform() = libraries[getCurrentNDIPlatform()]?.first() ?:
        throw IllegalStateException("Cannot find ndi library for platform: ${getCurrentNDIPlatform()}")
}