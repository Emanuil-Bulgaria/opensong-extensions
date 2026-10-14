package bg.emanuil.watcher

import com.ctc.wstx.exc.WstxEOFException
import com.ctc.wstx.exc.WstxException
import com.ctc.wstx.exc.WstxParsingException
import io.github.oshai.kotlinlogging.KLogging
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamException
import javax.xml.stream.XMLStreamReader

class OpenSongWatcher(
    private val documents: File,
                      private val notify: (SlideData) -> Unit) {

    private val logger = KotlinLogging.logger {}

    val file: File by lazy {
        val f = File(documents, "VMixOpenSong.xml")
        if(!f.exists()) throw IllegalStateException("VMixOpenSong.xml does not exists")
        f
    }

    val factory: XMLInputFactory by lazy { XMLInputFactory.newInstance() }

    suspend fun run() = withContext(Dispatchers.IO) {
        read()
        FileWatcher(file) { read() }.run()
    }

    private fun read() {
        file.inputStream().use { stream ->
            val reader = factory.createXMLStreamReader(stream)
            var title: String? = null
            var subtitle: String? = null
            var body: String? = null
            try {
                while (reader.hasNext()) {
                    val event = reader.next()
                    if (event == XMLStreamReader.START_ELEMENT) {
                        when(reader.localName) {
                            "body" -> body = reader.elementText.ifBlank { null }
                            "title" -> title = reader.elementText.ifBlank { null }
                            "subtitle" -> subtitle = reader.elementText.ifBlank { null }
                        }
                    }
                }
                notify(SlideData(title, subtitle, body))
            } catch (e: XMLStreamException) {
                logger.error(throwable = e, message = { "Failed parsing opensong xml" })
            }
        }
    }
}