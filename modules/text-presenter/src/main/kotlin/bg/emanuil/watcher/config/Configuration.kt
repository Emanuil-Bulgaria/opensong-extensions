package bg.emanuil.watcher.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Configuration(
    @SerialName("OpenSong")
    val openSongConfig: OpenSongConfig? = null,

    @SerialName("NDI")
    val ndiStreamConfig: List<NDIStreamConfig>? = null
)