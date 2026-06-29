package bg.emanuil


import bg.emanuil.draw.SkiaNDIStreamer
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Color
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect
import kotlin.math.sin

object TestRenderer : SkiaNDIStreamer() {

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

    override fun render(canvas: Canvas, frameTime: Long) {
        canvas.drawRect(Rect.makeWH(width.toFloat(), height.toFloat()), backgroundPaint)
        val delta = System.nanoTime() - startTime
        val y = height / 2f + (sin((delta/1_000_000_000.0f) * 3f) * 150f)
        canvas.drawCircle(width / 2f, y, 300f, circlePaint)
    }
}