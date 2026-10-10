package bg.emanuil.opensong.model.x_bible_format

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@SerialName("VERS")
data class Verse(
    @XmlSerialName("vnumber") val number: String,
    @XmlValue val text: String,
)
