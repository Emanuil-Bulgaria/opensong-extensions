package bg.emanuil


import bg.emanuil.config.KoinKotlinLogger
import bg.emanuil.ndi.NDIStreamManager
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

suspend fun main() {
    startKoin {
        modules(modules)
        logger(KoinKotlinLogger(Level.DEBUG))
    }.koin.get<NDIStreamManager>().run()
}