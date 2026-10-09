package bg.emanuil.presentation.compose_example

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ComposeExampleViewModel: ViewModel() {

    private val _state = MutableStateFlow("Hello Compose!")
    val state: StateFlow<String> = _state
}