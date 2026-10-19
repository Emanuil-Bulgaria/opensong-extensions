package bg.emanuil.watcher

import bg.emanuil.config.Service
import bg.emanuil.watcher.config.Configuration
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Path
import kotlin.time.Duration.Companion.seconds

class ConfigurationStore(configPath: File = Path.of("config.json").toFile())
    : Service {

    private val logger = KotlinLogging.logger {}

    private val scope = CoroutineScope(Dispatchers.IO)

    val state: StateFlow<Configuration> = fileWatcher(configPath)
        .map(::readFile)
        .stateIn(
            scope = scope,
            SharingStarted.WhileSubscribed(5000),
            Configuration())

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private suspend fun readFile(file: File): Configuration {
        delay(1.seconds)
        if(!file.exists()) {
            logger.error { "File ${file.absolutePath} does not exist." }
            return Configuration()
        }
        val raw = file.readText()
        return json.decodeFromString(raw)
    }

    override fun start() {
    }

    override fun stop() {
        scope.cancel()
    }
}