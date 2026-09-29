package bg.emanuil.watcher

import bg.emanuil.watcher.config.Configuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Path
import kotlin.time.Duration.Companion.seconds

class ConfigurationStore(configPath: File = Path.of("config.json").toFile()) {

    val state: StateFlow<Configuration> = fileWatcher(configPath)
        .map { readFile(it) }
        .stateIn(
            CoroutineScope(Dispatchers.IO),
            SharingStarted.WhileSubscribed(5000),
            Configuration())

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private suspend fun readFile(file: File): Configuration {
        delay(1.seconds)
        val raw = file.readText()
        return json.decodeFromString(raw)
    }
}