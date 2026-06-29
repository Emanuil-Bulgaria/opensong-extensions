package bg.emanuil

import bg.emanuil.ndi.NDIOutputFrame
import bg.emanuil.ndi.NDIOutputStream
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Color
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorInfo
import org.jetbrains.skia.ColorSpace
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.ImageInfo
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect
import org.jetbrains.skia.Surface
import kotlin.math.sin

object TestStream : NDIOutputStream {

    private lateinit var surface: Surface
    private lateinit var canvas: Canvas
    private var width: Int = 1920
    private var height: Int = 1080

    val backgroundPaint = Paint().apply {
        color = Color.makeRGB(0x00, 0xAD, 0xB5) // Teal
        isAntiAlias = true
    }

    val circlePaint = Paint().apply {
        color = Color.makeRGB(0xFF, 0xFF, 0xFF) // Teal
        isAntiAlias = true
    }
    val textPaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
    }

    private var startTime: Long = 1

    override fun initialize(frame: NDIOutputFrame, startTime: Long) {
        width = frame.dimensions.width
        height = frame.dimensions.height
        surface = frame.surface()
        canvas = surface.canvas
        TestStream.startTime = startTime
    }

    override fun render(frame: NDIOutputFrame, frameStartTime: Long) {
        canvas.drawRect(Rect.makeWH(width.toFloat(), height.toFloat()), backgroundPaint)
        val delta = System.nanoTime() - startTime
        val y = height / 2f + (sin((delta/1_000_000_000.0f) * 3f) * 150f)
        canvas.drawCircle(width / 2f, y, 300f, circlePaint)
    }

    private fun NDIOutputFrame.surface(): Surface {
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