package bg.emanuil.opensong.model

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("bible")
data class Bible(
    @XmlElement(true) @XmlSerialName("b") val books: List<Book>,
)
