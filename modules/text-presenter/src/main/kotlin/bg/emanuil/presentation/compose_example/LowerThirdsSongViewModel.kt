package bg.emanuil.presentation.compose_example

import androidx.lifecycle.ViewModel
import bg.emanuil.draw.OpenSongRepository

class LowerThirdsSongViewModel(
    repository: OpenSongRepository
): ViewModel() {
    val text = repository.body
}