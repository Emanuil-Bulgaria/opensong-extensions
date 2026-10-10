package bg.emanuil.watcher

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchService
import kotlin.io.path.absolute
import kotlin.io.path.name

fun fileTracker(file: File) = callbackFlow {
    val path = file.toPath().absolute()

    var watcherService: WatchService? = null
    launch(Dispatchers.IO) {
        watcherService = FileSystems.getDefault().newWatchService()
        path.parent.register(watcherService, StandardWatchEventKinds.ENTRY_MODIFY)
        while (true) {
            val key = watcherService.take()
            for (event in key.pollEvents()) {
                if (event.context().toString().contains(path.name)) {
                    trySend(file)
                }
            }
            key.reset()
        }
    }

    awaitClose {
        watcherService?.close()
    }
}