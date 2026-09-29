package bg.emanuil.ndi

import bg.emanuil.LowerThirdsExample
import bg.emanuil.draw.SkiaNDIStreamer
import bg.emanuil.ndi.impl.NdiLibrary
import bg.emanuil.watcher.ConfigurationStore
import bg.emanuil.watcher.config.NDIStreamConfig
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import java.lang.foreign.Arena

class NDIStreamManager(
    private val configStore: ConfigurationStore,
    private val templates: Map<String, SkiaNDIStreamer>
) {
    private val logger = KotlinLogging.logger {}

    private var jobs: MutableMap<String, NDIStreamJob> = mutableMapOf()

    suspend fun run() {
        Arena.ofShared().use { arena ->
            NdiLibrary(arena).use { ndi ->
                ndi.initialize()
                logger.info { "NDI Version ${ndi.version()}" }
                println(ndi.version())

                configStore.state
                    .map { it.ndiStreamConfig }
                    .collect { config ->
                        if(config != null) {
                            val streamNames = config.mapNotNull { it.name }
                            val removed = jobs.keys.filter { !streamNames.contains(it) }
                            removed.forEach {
                                jobs[it]?.cancel()
                                jobs.remove(it)
                            }

                            config.forEach { config ->
                                startStream(ndi, config)
                            }
                        }
                    }
            }
        }
    }

    private fun startStream(ndi: NdiLibrary, config: NDIStreamConfig?) {
        val name = config?.name
        val templateName = config?.template
        if(name == null) {
            logger.error { "No NDI configuration specified!" }
            return
        }
        if (templateName == null) {
            logger.error { "Invalid template for NDI stream $name!" }
            return
        }

        val template = templates[templateName]
        if(template == null) {
            logger.error {
                "Template with $templateName for NDI stream $name does not exist! Valid names are: ${templates.keys}"
            }
            return
        }

        val job = jobs[name]
        if(job != null) {
            logger.info { "Re-configured NDI stream: $name" }
        }
        job?.cancel()
        val newJob = NDIOutputStreamer(ndi,
            name = name,
            dimensions = Dimensions(width = 1920, height = 360)
        ).run {
            add(template)
            start().apply {
                logger.info { "Started NDI stream: $name with template ($templateName)" }
            }
        }
        jobs[name] = NDIStreamJob(newJob, templateName)
    }
}