package bg.emanuil

import bg.emanuil.draw.SkiaNDIStreamer
import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Color
import org.jetbrains.skia.Font
import org.jetbrains.skia.FontMgr
import org.jetbrains.skia.FontStyle
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect
import kotlin.math.sin

object SimpleTextRendererExample : SkiaNDIStreamer() {
    val typeface = FontMgr.default.matchFamilyStyle("Arial", FontStyle.NORMAL)
    val font = Font(typeface, 24f)

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
        canvas.drawRect(
            Rect.makeWH(width.toFloat(), height.toFloat()), backgroundPaint
        )

        val timeSec = startTime / 1_000_000_000.0
        val centerX = width / 2f
        val centerY = height / 2f + (sin(timeSec * 3.0) * 50f).toFloat()

        canvas.drawCircle(centerX, centerY, 80f, circlePaint)

        canvas.drawString("Message", 40f, 60f, font, textPaint)
    }
}