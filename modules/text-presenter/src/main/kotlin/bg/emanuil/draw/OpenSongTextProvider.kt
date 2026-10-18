package bg.emanuil.draw

import bg.emanuil.config.Service
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

class OpenSongTextProvider(
    private val configStore: ConfigurationStore
): TextProvider, Service {

    private val logger = KotlinLogging.logger {}

    private val _state = MutableStateFlow(mapOf<String, String?>(
        "OpenSongSlideBody" to null,
        "OpenSongSlideTitle" to null,
    ))

    override val text: Flow<Map<String, String?>> = _state.asStateFlow()

    private var job: Job? = null

    private var serviceJob: Job? = null

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun start() {
        serviceJob = CoroutineScope(Dispatchers.IO).launch {
            configStore.state
                .map { it.openSongConfig }
                .collect { config ->
                    watchFile(config)
                }
        }
    }

    fun watchFile(config: OpenSongConfig?) {
        job?.cancel()
        job = scope.launch {
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
                _state.update {
                    mapOf(
                        "OpenSongSlideBody" to slideData.body,
                        "OpenSongSlideTitle" to slideData.title,
                    )
                }
            }.run()
        }
    }

    override fun stop() {
        job?.cancel()
        serviceJob?.cancel()
    }
}