package bg.emanuil

import bg.emanuil.draw.SkiaNDIStreamer
import bg.emanuil.watcher.OpenSongWatcher
import org.jetbrains.skia.*
import org.jetbrains.skia.paragraph.*
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.atomic.AtomicReference

object LowerThirdsExample : SkiaNDIStreamer() {
    val typeface = FontMgr.default.matchFamilyStyle("Arial", FontStyle.NORMAL)

    val atomicString = AtomicReference("Hello World!")

    val backgroundPaint = Paint().apply {
        color = Color.makeARGB(0x00, 0xAD, 0x00, 0x00) // Teal
        isAntiAlias = true
    }

    fun enableTracking(executor: ExecutorService) {
        executor.submit(OpenSongWatcher(File("C:\\Users\\vmtest\\Documents")) {
            it.body?.let(atomicString::set)
        })
    }

    override fun render(canvas: Canvas, frameTime: Long) {
        canvas.clear(Color.makeARGB(0x00, 0xAD, 0x00, 0x00))

        TextMulti(canvas, atomicString.get())
    }

    fun TextMulti(canvas: Canvas, text: String) {
        val fontCollection = FontCollection().apply {
            setDefaultFontManager(FontMgr.default)
        }

        val paragraphStyle = ParagraphStyle().apply {
            alignment = Alignment.CENTER
        }

        var paragraph: Paragraph? = null
        for(size in listOf(72f, 64f, 36f, 18f)) {
            val textStyle = TextStyle().apply {
                color = Color.WHITE
                fontSize = size
            }

            paragraph = ParagraphBuilder(paragraphStyle, fontCollection).apply {
                pushStyle(textStyle)
                addText(text)
            }.build()

            paragraph.layout(width - 80f)

            if(paragraph.height <= height) {
                break
            }
        }

        if(paragraph != null) {
            val y = height / 2f - paragraph.height / 2f
            val x = 40f
            paragraph.paint(canvas, x, y)
        }
    }

    fun Text(canvas: Canvas, text: String,
             font: Font = Font(typeface, 72f),
             strokeColor: Int = Color.BLACK,
             textColor: Int = Color.WHITE,
             stroke: Float = 0f) {

        val paint = Paint().apply {
            color = textColor
            isAntiAlias = true
        }
        val rect = font.measureText(text, paint)

        val x = (width / 2f) - (rect.width / 2f)
        val y = (height / 2f) - (rect.height / 2f)
        if(stroke > 0) {
            val outline = Paint().apply {
                color = strokeColor
                strokeWidth = stroke
                isAntiAlias = true
            }
            canvas.drawString(text, x, y, font, outline)
        }
        canvas.drawString(text, 0f, 300f, font, paint)
    }
}