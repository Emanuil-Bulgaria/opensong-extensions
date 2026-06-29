package bg.emanuil.watcher

import java.io.File
import java.nio.file.FileSystems
import java.nio.file.StandardWatchEventKinds
import kotlin.io.path.absolute
import kotlin.io.path.name

class FileWatcher(private val file: File,
                  private val notify: (File) -> Unit = {}) : Runnable {

    private val path = file.toPath().absolute()

    override fun run() {
        val watcherService = FileSystems.getDefault().newWatchService()

        path.parent.register(watcherService, StandardWatchEventKinds.ENTRY_MODIFY)

        while (true) {
            val key = watcherService.take()
            for (event in key.pollEvents()) {
                if (event.context().toString().contains(path.name)) {
                    notify(file)
                }
            }
            key.reset()
        }
    }
}