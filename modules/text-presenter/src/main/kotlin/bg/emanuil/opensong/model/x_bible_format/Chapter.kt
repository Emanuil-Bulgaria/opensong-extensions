package bg.emanuil.opensong.model.x_bible_format

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("CHAPTER")
data class Chapter(
    @XmlSerialName("cnumber") val number: String,
    @XmlElement(true) @XmlSerialName("VERS") val verses: List<Verse>,
)
