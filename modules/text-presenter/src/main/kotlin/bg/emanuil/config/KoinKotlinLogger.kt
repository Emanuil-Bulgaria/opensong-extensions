package bg.emanuil.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.koin.core.logger.Level
import org.koin.core.logger.Logger

class KoinKotlinLogger(
    level: Level = Level.INFO
) : Logger(level) {

    private val logger = KotlinLogging.logger("Koin")

    override fun display(level: Level, msg: String) {
        when (level) {
            Level.DEBUG -> logger.debug { msg }
            Level.INFO -> logger.info { msg }
            Level.WARNING -> logger.warn { msg }
            Level.ERROR -> logger.error { msg }
            Level.NONE -> { /* Do nothing */ }
        }
    }
}