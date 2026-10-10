package bg.emanuil.opensong.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@SerialName("v")
data class Verse(
    @XmlSerialName("n") val number: String,
    @XmlValue val text: String,
)
