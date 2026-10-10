package bg.emanuil.opensong

import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.File

class OpenSongVerseSearcher(documents: File) {

    private val logger = KotlinLogging.logger {}

    val maps: List<Map<String, String>>

    init {
        val f = File(documents, "OpenSong Scripture")
        val bibles = f.listFiles().filter { it.isFile && it.name.endsWith(".xml") }
        logger.info { "Found the following bibles ${bibles.map { it.name }}" }
        maps = bibles
            .map { OpenSongXMLParser.parseScriptureXML(it) }
            .map { it.toVerseMap() }
    }

    operator fun invoke(term: String): String? {
        logger.debug { "Term: $term" }
        for(map in maps) {
            map[term.trim()]?.let {
                logger.debug { "Term result: $it" }
                return it
            }
        }
        logger.debug { "Term result: null" }
        return null
    }
}