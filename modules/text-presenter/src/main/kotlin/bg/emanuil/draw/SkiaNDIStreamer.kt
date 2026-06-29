package bg.emanuil.draw

import bg.emanuil.ndi.NDIOutputFrame
import bg.emanuil.ndi.NDIOutputStream
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorInfo
import org.jetbrains.skia.ColorSpace
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.ImageInfo
import org.jetbrains.skia.Surface

abstract class SkiaNDIStreamer : NDIOutputStream {

    private lateinit var surface: Surface
    private lateinit var canvas: Canvas
    protected var width: Int = 1920
        private set
    protected var height: Int = 1080
        private set
    protected var startTime: Long = 0
        private set

    override fun initialize(frame: NDIOutputFrame, startTime: Long) {
        width = frame.dimensions.width
        height = frame.dimensions.height
        surface = frame.surface()
        canvas = surface.canvas
        this.startTime = startTime
    }

    abstract fun render(canvas: Canvas, frameTime: Long)

    final override fun render(frame: NDIOutputFrame, frameStartTime: Long) {
        render(canvas, frameStartTime)
    }

    override fun destroy() {
    }

    protected fun NDIOutputFrame.surface(): Surface {
        val colorInfo = ColorInfo(
            ColorType.RGBA_8888,
            ColorAlphaType.PREMUL,
            ColorSpace.sRGB)
        val imageInfo = ImageInfo(colorInfo, this.dimensions.width,
            this.dimensions.height)
        val rowBytes = this.dimensions.width * 4

        return Surface.makeRasterDirect(
            imageInfo = imageInfo,
            pixelsPtr = this.data.address(),
            rowBytes = rowBytes,
        )
    }
}