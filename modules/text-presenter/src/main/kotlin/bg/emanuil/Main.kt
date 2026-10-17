package bg.emanuil


import androidx.compose.ui.window.application
import bg.emanuil.config.KoinKotlinLogger
import bg.emanuil.ndi.NDIStreamManager
import io.github.oshai.kotlinlogging.KotlinLogging
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import kotlin.system.exitProcess

suspend fun main() {
    val logger = KotlinLogging.logger {}
    try {
        startKoin {
            modules(modules)
            logger(KoinKotlinLogger(Level.DEBUG))
        }.koin.get<NDIStreamManager>().run()
        application {

        }
    } catch (e: Exception) {
        logger.error(e) { "Error on starting application" }
        exitProcess(1);
    }
}