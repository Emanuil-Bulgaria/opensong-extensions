package bg.emanuil.presentation.compose_example

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LowerThirdsSong(
    viewModel: LowerThirdsSongViewModel = koinViewModel()
) {

    val text by viewModel.text.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center) {
        text?.let { text ->
            BasicText(
                text = text,
                maxLines = 2,
                autoSize = TextAutoSize.StepBased(
                    minFontSize = 76.sp,
                    maxFontSize = 96.sp,
                    stepSize = 1.sp
                )
            )
        }
    }
}