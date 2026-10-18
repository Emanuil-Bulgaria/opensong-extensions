package bg.emanuil.examples

import bg.emanuil.draw.SkiaNDIStreamer
import bg.emanuil.draw.TextProvider
import bg.emanuil.ndi.NDIOutputFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.jetbrains.skia.*
import org.jetbrains.skia.paragraph.*

class LowerThirdsExample(
    textProviders: List<TextProvider>,
) : SkiaNDIStreamer() {

    val texts = textProviders
        .map { it.text }
        .run { combine(this) {
            val r: Map<String, String?> = it
                .asList()
                .fold(mapOf()) { acc, map -> map + acc }
            r
        } }
        .stateIn(
            CoroutineScope(Dispatchers.IO),
            SharingStarted.Eagerly,
            emptyMap())

    val typeface = FontMgr.default.matchFamilyStyle("Arial", FontStyle.NORMAL)

    override fun initialize(frame: NDIOutputFrame, startTime: Long) {
        super.initialize(frame, startTime)
    }

    override fun render(canvas: Canvas, frameTime: Long) {
        canvas.clear(Color.makeARGB(0x00, 0xAD, 0x00, 0x00))

        texts.value["OpenSongSlideBody"]?.let { text ->
            TextMulti(canvas, text)
        }
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