package bg.emanuil.watcher.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenSongConfig(
    @SerialName("DocumentsDirectory")
    val documentsPath: String? = null)