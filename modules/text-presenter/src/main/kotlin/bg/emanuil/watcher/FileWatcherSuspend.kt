package bg.emanuil.watcher

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.ClosedWatchServiceException
import java.nio.file.FileSystems
import java.nio.file.StandardWatchEventKinds
import kotlin.coroutines.cancellation.CancellationException
import kotlin.io.path.absolute
import kotlin.io.path.name

fun fileWatcher(file: File) = callbackFlow {
    send(file)
    val path = file.toPath().absolute()
    val watcherService = FileSystems.getDefault().newWatchService()
    path.parent.register(watcherService, StandardWatchEventKinds.ENTRY_MODIFY)

    val job = launch {
        try {
            while (true) {
                val key = watcherService.take()
                for (event in key.pollEvents()) {
                    if (event.context().toString().contains(path.name)) {
                        send(file)
                    }
                }
                key.reset()
            }
        } catch (e: ClosedWatchServiceException) {

        }
    }

    awaitClose {
        job.cancel()
        watcherService.close()
    }
}.flowOn(Dispatchers.IO)
