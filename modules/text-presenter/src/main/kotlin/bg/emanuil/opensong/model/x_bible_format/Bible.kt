package bg.emanuil.opensong.model.x_bible_format

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

typealias OpenBibleFormat = Bible

@Serializable
@XmlSerialName("XMLBIBLE")
data class Bible(
    @XmlElement(true) @XmlSerialName("BIBLEBOOK") val books: List<Book>,
)
