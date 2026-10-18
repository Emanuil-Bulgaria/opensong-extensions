package bg.emanuil

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
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
import bg.emanuil.config.modules
import bg.emanuil.presentation.lower_thirds_scripture.LowerThirdsScripture
import bg.emanuil.presentation.lower_thirds_scripture.LowerThirdsScriptureInternal
import bg.emanuil.presentation.lower_thirds_song.LowerThirdsSong
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
        Window(
            resizable = false,
            onCloseRequest = ::exitApplication,
        ) {
            Win(modifier = Modifier.size(1920.dp, 1080.dp))
        }
    }
}

@Composable
@Preview
fun Win(modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalViewModelStoreOwner provides storeOwner) {
        val imageBitmap: ImageBitmap = remember {
            (object {}.javaClass::getResourceAsStream)("/Screenshot 2026-10-14 180250.png")!!
                .readBytes()
                .decodeToImageBitmap()
        }
        Box(
            modifier = modifier.fillMaxSize(),
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
                LowerThirdsScriptureInternal(
                    title = "Числа 23:19",
                    body = "Бог не е човек, за да лъже, " +
                            "нито човешки син, за да се разкае; " +
                            "Той каза и няма ли да извърши? " +
                            "Той говори и няма ли да го приведе в действие?",
                )
            }
        }
    }
}