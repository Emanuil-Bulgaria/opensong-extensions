package bg.emanuil.watcher.config

import kotlinx.serialization.Serializable

@Serializable
data class NDIStreamConfig(
    val name: String? = null,
    val template: String? = null,
)
