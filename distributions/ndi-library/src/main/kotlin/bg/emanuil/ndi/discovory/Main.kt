package bg.emanuil.ndi.discovory

import java.io.File
import java.lang.foreign.Arena
import java.lang.foreign.FunctionDescriptor
import java.lang.foreign.Linker
import java.lang.foreign.MemorySegment
import java.lang.foreign.SymbolLookup
import java.lang.foreign.ValueLayout
import java.nio.file.Path
import java.util.Properties
import kotlin.jvm.optionals.getOrNull

fun main(args: Array<String>) {

    if (args.size != 1) {
        throw IllegalArgumentException("Please specify the NDI runtime path")
    }

    val ndiRuntimePath = File(args[0])

    val libraryPath = findLibrary(ndiRuntimePath, "Processing.NDI.Lib.x64.dll")

    val info = getLibraryInfo(libraryPath)

    println(info)

    writeProperties(info)
}

fun writeProperties(info: NdiInfo) {
    val name = when (info.platform) {
        "WIN64" -> "windows-x86_64.properties"
        else -> throw IllegalArgumentException("Unsupported platform: ${info.platform}")
    }
    val path = Path.of("./ndi-info/$name")
    path.parent.toFile().mkdirs()
    val props = Properties()
    props.setProperty("platform", info.platform)
    props.setProperty("date", info.date)
    props.setProperty("version", info.version)
    props.setProperty("path", info.libraryPath.absoluteFile.toString())

    path.toFile().writer().use { writer ->
        props.store(writer, "NDI SDK Build Details")
    }
}

private fun getLibraryInfo(libraryPath: File): NdiInfo {
    val info = Arena.ofConfined().use { arena ->
        val lookup = SymbolLookup.libraryLookup(libraryPath.toPath(), arena)
        val linker = Linker.nativeLinker()
        val address = lookup.find("NDIlib_version").getOrNull()
            ?: throw IllegalStateException("Cannot find address for symbol NDIlib_version")

        val handle = linker
            .downcallHandle(address, FunctionDescriptor.of(ValueLayout.ADDRESS))

        val result = handle.invoke() as MemorySegment
        result.reinterpret(Long.MAX_VALUE).getString(0L)
    }

    val regex = """(WIN\d+|MAC|LINUX)\s+(\d{2}:\d{2}:\d{2}\s+[A-Za-z]{3}\s+\d{2}\s+\d{4})\s+([\d.]+)""".toRegex()
    val matchResult = regex.find(info)

    if (matchResult != null) {
        val (platform, date, version) = matchResult.destructured
        return NdiInfo(platform, version, date, libraryPath)
    } else {
        throw IllegalStateException("Unknown version format: $info")
    }
}

fun findLibrary(ndiRuntimePath: File, target: String): File {
    val result = ndiRuntimePath.walk().filter { file ->
        file.isFile && file.extension == "dll" && file.name == target
    }.toList()
    if (result.isEmpty()) {
        throw IllegalStateException("Couldn't find $target in $ndiRuntimePath")
    } else if (result.size > 1) {
        throw IllegalStateException("Found multiple $target  in $ndiRuntimePath: $result")
    }
    return result[0]
}

data class NdiInfo(val platform: String, val version: String, val date: String, val libraryPath: File) {
    override fun toString(): String {
        return "$platform $date $version"
    }
}