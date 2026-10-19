package bg.emanuil.draw

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import bg.emanuil.ndi.NDIOutputFrame
import bg.emanuil.ndi.NDIOutputStream
import org.jetbrains.skia.*

open class ComposeNDIStreamer(private val content: @Composable () -> Unit) : NDIOutputStream {

    private lateinit var scene: ImageComposeScene

    private val colorInfo = ColorInfo(
        ColorType.RGBA_8888,
        ColorAlphaType.PREMUL,
        ColorSpace.sRGB)

    private lateinit var imageInfo: ImageInfo
    private var rowBytes: Int = 0

    private val storeOwner = object: ViewModelStoreOwner {
        override val viewModelStore = ViewModelStore()
    }


    override fun initialize(frame: NDIOutputFrame, startTime: Long) {
        super.initialize(frame, startTime)
        scene = ImageComposeScene(
            width = frame.dimensions.width,
            height = frame.dimensions.height,
            content = {
                CompositionLocalProvider(LocalViewModelStoreOwner provides storeOwner) {
                    content()
                }
            }
        )
        imageInfo = ImageInfo(
            colorInfo,
            frame.dimensions.width,
            frame.dimensions.height,
        )

        rowBytes = frame.dimensions.width * 4
    }

    private val imageDrawPaint = Paint().apply {
        blendMode = BlendMode.SRC
    }

    override fun render(frame: NDIOutputFrame, frameStartTime: Long) {
        frame.surface().use { surface ->
            scene.render(frameStartTime).use { image ->
                surface.canvas.drawImage(image, 0f, 0f, imageDrawPaint)
            }
        }
    }

    private fun NDIOutputFrame.surface() = Surface.makeRasterDirect(
            imageInfo = imageInfo,
            pixelsPtr = this.data.address(),
            rowBytes = rowBytes
    )
}