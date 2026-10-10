package bg.emanuil.opensong.model

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("b")
data class Book(
    @XmlSerialName("n") val name: String,
    @XmlElement(true) @XmlSerialName("c") val chapters: List<Chapter>,
)
