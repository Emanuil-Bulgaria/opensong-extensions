package bg.emanuil


import bg.emanuil.ndi.Dimensions
import bg.emanuil.ndi.NDIOutputStreamer
import bg.emanuil.ndi.impl.NdiLibrary
import bg.emanuil.watcher.FileWatcher
import bg.emanuil.watcher.OpenSongWatcher
import java.io.File
import java.lang.foreign.Arena
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main() {


    val service = Executors.newFixedThreadPool(2)

    Arena.ofShared().use { arena ->
        NdiLibrary(arena).use { ndi ->
            ndi.initialize()
            println(ndi.version())

            val streamer = NDIOutputStreamer(ndi, name = "Test NDI",
                dimensions = Dimensions(width = 1920, height = 360))
            streamer.add(LowerThirdsExample)

            LowerThirdsExample.enableTracking(service)
            service.submit(streamer)

            service.awaitTermination(1000, TimeUnit.DAYS)

            streamer.run()
        }
    }
}