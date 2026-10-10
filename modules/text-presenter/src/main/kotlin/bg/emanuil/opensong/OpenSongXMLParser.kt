package bg.emanuil.opensong

import bg.emanuil.opensong.model.Bible
import bg.emanuil.opensong.model.x_bible_format.OpenBibleFormat
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.XmlException
import nl.adaptivity.xmlutil.serialization.XML
import java.io.File

object OpenSongXMLParser {

    private val lenient = XML.recommended_1_0 { policy { ignoreUnknownChildren() } }

    fun parseScriptureXML(file: File): Bible {
        val data = file.readText()
        try {
            return lenient.decodeFromString<Bible>(data)
        } catch (e: XmlException) {
            val openFormat = lenient.decodeFromString<OpenBibleFormat>(data)
            return openFormat.toBible()
        }
    }
}