package bg.emanuil.watcher

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.StandardWatchEventKinds
import kotlin.io.path.absolute
import kotlin.io.path.name

fun fileWatcher(file: File) = flow {
    emit(file)
    val path = file.toPath().absolute()
    val watcherService = FileSystems.getDefault().newWatchService()
    path.parent.register(watcherService, StandardWatchEventKinds.ENTRY_MODIFY)
    while (true) {
        val key = watcherService.take()
        for (event in key.pollEvents()) {
            if (event.context().toString().contains(path.name)) {
                emit(file)
            }
        }
        key.reset()
    }
}.flowOn(Dispatchers.IO)
