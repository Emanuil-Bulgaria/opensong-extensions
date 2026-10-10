package bg.emanuil.opensong.model.x_bible_format

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("BIBLEBOOK")
data class Book(
    @XmlSerialName("bname") val name: String,
    @XmlElement(true) @XmlSerialName("CHAPTER") val chapters: List<Chapter>,
)
