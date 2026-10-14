package bg.emanuil

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import bg.emanuil.config.KoinKotlinLogger
import bg.emanuil.presentation.compose_example.LowerThirdsScripture
import bg.emanuil.presentation.compose_example.LowerThirdsSong
import org.koin.compose.KoinApplication
import org.koin.core.logger.Level
import java.io.File

private val storeOwner = object: ViewModelStoreOwner {
    override val viewModelStore = ViewModelStore()
}


fun main() = application {
    KoinApplication(application = {
        modules(modules)
        logger(KoinKotlinLogger(Level.DEBUG))
    }) {
        Window(onCloseRequest = ::exitApplication) {
            Win()
        }
    }
}

@Composable
@Preview
fun Win() {
    CompositionLocalProvider(LocalViewModelStoreOwner provides storeOwner) {
        val imageBitmap: ImageBitmap = remember {
            File("C:\\Users\\Presenter\\Desktop\\Screenshot 2026-10-14 180250.png")
                .readBytes()
                .decodeToImageBitmap()
        }
        Box(
            modifier = Modifier.fillMaxSize(),

            ) {
            Image(
                bitmap = imageBitmap,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
            Box(
                modifier = Modifier.height(300.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
            ) {
                LowerThirdsSong()
            }
        }
    }
}