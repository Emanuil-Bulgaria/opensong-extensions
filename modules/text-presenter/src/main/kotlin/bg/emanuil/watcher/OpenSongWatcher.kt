package bg.emanuil.watcher

import java.io.File
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamReader

class OpenSongWatcher(
    private val documents: File,
                      private val notify: (SlideData) -> Unit) : Runnable {
    val file: File by lazy {
        val f = File(documents, "VMixOpenSong.xml")
        if(!f.exists()) throw IllegalStateException("VMixOpenSong.xml does not exists")
        f
    }

    val factory: XMLInputFactory by lazy { XMLInputFactory.newInstance() }

    override fun run() {
        read()
        FileWatcher(file) { read() }.run()
    }

    private fun read() {
        file.inputStream().use { stream ->
            val reader = factory.createXMLStreamReader(stream)
            var title: String? = null
            var subtitle: String? = null
            var body: String? = null
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
        }
    }
}