package bg.emanuil

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import bg.emanuil.config.KoinKotlinLogger
import bg.emanuil.presentation.compose_example.LowerThirdsScripture
import org.koin.compose.KoinApplication
import org.koin.core.logger.Level

private val storeOwner = object: ViewModelStoreOwner {
    override val viewModelStore = ViewModelStore()
}


fun main() = application {
    KoinApplication(application = {
        modules(modules)
        logger(KoinKotlinLogger(Level.DEBUG))
    }) {
        Window(onCloseRequest = ::exitApplication) {
            CompositionLocalProvider(LocalViewModelStoreOwner provides storeOwner) {
                LowerThirdsScripture()
            }

        }
    }
}