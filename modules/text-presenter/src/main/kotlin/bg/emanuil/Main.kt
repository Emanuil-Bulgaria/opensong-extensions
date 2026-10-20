package bg.emanuil


import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.isTraySupported
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import bg.emanuil.config.KoinKotlinLogger
import bg.emanuil.config.modules
import io.github.oshai.kotlinlogging.KotlinLogging
import org.koin.compose.KoinApplication
import org.koin.core.logger.Level
import kotlin.system.exitProcess

private val storeOwner = object: ViewModelStoreOwner {
    override val viewModelStore = ViewModelStore()
}

fun main() {
    val logger = KotlinLogging.logger {}
    try {
        application {
            KoinApplication(
                application = {
                    modules(modules)
                    logger(KoinKotlinLogger(Level.DEBUG))
                }
            ) {
                val icon = rememberVectorPainter(Icons.Default.Android)
                if(isTraySupported) {
                    Tray(icon = icon) {
                        Item("Exit", onClick = ::exitApplication)
                    }
                } else {
                    Window(
                        icon = icon,
                        title = "NDI Streamer",
                        onCloseRequest = ::exitApplication) {
                        Button(onClick = ::exitApplication) {
                            Text(text = "Exit")
                        }
                    }
                }
            }
        }
    } catch (e: Exception) {
        logger.error(e) { "Error on starting application" }
        exitProcess(1);
    }
}