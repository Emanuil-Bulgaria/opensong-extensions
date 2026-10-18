package bg.emanuil.presentation.lower_thirds_scripture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bg.emanuil.draw.OpenSongRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class LowerThirdsScriptureViewModel(
    repository: OpenSongRepository
): ViewModel() {

    private val logger = KotlinLogging.logger {}

    val body = repository.body
    val title = body
        .filterNotNull()
        .map(repository::getVerse)
        .onEach { logger.info { "Title: $it" } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}