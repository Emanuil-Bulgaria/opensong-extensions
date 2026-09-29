package bg.emanuil.draw

import kotlinx.coroutines.flow.Flow

interface TextProvider {
    val text: Flow<Map<String, String?>>
}