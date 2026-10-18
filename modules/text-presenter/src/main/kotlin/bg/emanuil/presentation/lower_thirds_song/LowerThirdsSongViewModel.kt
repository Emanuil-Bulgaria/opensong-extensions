package bg.emanuil.presentation.lower_thirds_song

import androidx.lifecycle.ViewModel
import bg.emanuil.draw.OpenSongRepository

class LowerThirdsSongViewModel(
    repository: OpenSongRepository
): ViewModel() {
    val text = repository.body
}