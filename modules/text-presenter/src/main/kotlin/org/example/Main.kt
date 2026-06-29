package org.example

import org.jetbrains.skia.*
import org.jetbrains.skiko.SkiaLayer
import org.jetbrains.skiko.SkikoRenderDelegate
import java.awt.Dimension
import java.lang.foreign.Arena
import java.util.ServiceLoader
import java.util.concurrent.TimeUnit
import javax.swing.JFrame
import javax.swing.WindowConstants
import kotlin.math.sin

fun main() {
    val skiaLayer = SkiaLayer()

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

    skiaLayer.renderDelegate = object : SkikoRenderDelegate {



        val typeface = FontMgr.default.matchFamilyStyle("Arial", FontStyle.NORMAL)
        val font = Font(typeface, 24f)

        override fun onRender(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
            canvas.drawRect(
                Rect.makeWH(width.toFloat(), height.toFloat()), backgroundPaint
            )

            val timeSec = nanoTime / 1_000_000_000.0
            val centerX = width / 2f
            val centerY = height / 2f + (sin(timeSec * 3.0) * 50f).toFloat()

            canvas.drawCircle(centerX, centerY, 80f, circlePaint)

            canvas.drawString("Message", 40f, 60f, font, textPaint)

            skiaLayer.needRender()
        }


    }

    val frame = JFrame("Test").apply {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        minimumSize = Dimension(800, 600)
    }
    frame.contentPane.add(skiaLayer)

    frame.pack()
    frame.setLocationRelativeTo(null)
    frame.isVisible = true

    Arena.ofConfined().use { arena ->
        NdiLibrary(arena).use { ndi ->
            ndi.initialize()
            println(ndi.version())

            ndi.sendCreate(NDISendCreate("Test NDI")).use { instance ->
                val width = 1920
                val height = 1080

                val bufferSize = width * height * 4L

                val dataPtr = arena.allocate(bufferSize)

                val frame = NDIVideoFrameV2(
                    xRes = width, yRes = height,
                    frameRateN = 60000, frameRateD = 1000,
                    pictureAspectRatio = 16f / 9,
                    fourCC = NDIFourCCVideoType.RGBA,
                    lineStrideInBytes = width * 4,
                    data = dataPtr
                )
                val colorInfo = ColorInfo(
                    ColorType.RGBA_8888,
                    ColorAlphaType.PREMUL,
                    ColorSpace.sRGB)
                val imageInfo = ImageInfo(colorInfo, width, height)
                val rowBytes = width * 4

                val surface = Surface.makeRasterDirect(
                    imageInfo = imageInfo,
                    pixelsPtr = dataPtr.address(),
                    rowBytes = rowBytes,
                )

                val canvas = surface.canvas

                val frameDuration = 1_000_000_000.0 / 30
                var frames = 0
                var startTime = System.nanoTime()
                while (true) {
                    val frameStart = System.nanoTime()
                    frames++
                    canvas.drawRect(Rect.makeWH(width.toFloat(), height.toFloat()), backgroundPaint)
                    val y = height / 2f + (sin((System.nanoTime()/1_000_000_000.0f) * 3f) * 50f)
                    canvas.drawCircle(width / 2f, y, 300f, circlePaint)
                    instance.send(frame)

                    val waitTime = frameDuration - (System.nanoTime() - frameStart)
                    if(waitTime > 0) {
                        TimeUnit.NANOSECONDS.sleep(waitTime.toLong())
                    }

                    if((System.nanoTime() - startTime) > 1_000_000_000) {
                        startTime = System.nanoTime()
                        println(frames)
                        frames = 0
                    }

                }
            }
        }
    }
}