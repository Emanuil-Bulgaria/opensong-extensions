package bg.emanuil.opensong.model

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("c")
data class Chapter(
    @XmlSerialName("n") val number: String,
    @XmlElement(true) @XmlSerialName("v") val verses: List<Verse>,
)
