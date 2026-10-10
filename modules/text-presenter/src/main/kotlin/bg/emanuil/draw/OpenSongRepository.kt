package bg.emanuil.draw

import bg.emanuil.watcher.ConfigurationStore
import bg.emanuil.watcher.OpenSongWatcher
import bg.emanuil.watcher.config.OpenSongConfig
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class OpenSongRepository(
    private val configStore: ConfigurationStore
) {

    private val logger = KotlinLogging.logger {}

    private val _title: MutableStateFlow<String?> = MutableStateFlow(null)

    private val _body: MutableStateFlow<String?> = MutableStateFlow(null)

    val title = _title.asStateFlow()
    val body = _body.asStateFlow()

    private var job: Job? = null

    fun start() {
        CoroutineScope(Dispatchers.IO).launch {
            configStore.state
                .map { it.openSongConfig }
                .collect { config ->
                    watchFile(config)
                }
        }
    }

    fun watchFile(config: OpenSongConfig?) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            val file = config?.documentsPath?.run { File(this) }
            if(file == null) {
                logger.error { "OpenSong data directory is not set!" }
                return@launch
            }
            if(!file.exists()) {
                logger.error { "OpenSong data directory does not exist!" }
                return@launch
            }

            OpenSongWatcher(file) { slideData ->
                _title.update { slideData.title }
                _body.update {
                    slideData.body }
                logger.info { "Update $slideData" }
            }.run()
        }
    }
}