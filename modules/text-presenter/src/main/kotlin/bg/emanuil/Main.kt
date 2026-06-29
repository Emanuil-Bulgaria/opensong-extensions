package bg.emanuil


import bg.emanuil.ndi.NDIOutputStreamer
import org.jetbrains.skia.Color
import org.jetbrains.skia.Paint
import org.jetbrains.skiko.SkiaLayer
import java.lang.foreign.Arena

fun main() {



    Arena.ofConfined().use { arena ->
        NdiLibrary(arena).use { ndi ->
            ndi.initialize()
            println(ndi.version())

            val streamer = NDIOutputStreamer(ndi, name = "Test NDI")
            streamer.add(SimpleTextRendererExample)

            streamer.run()
        }
    }
}