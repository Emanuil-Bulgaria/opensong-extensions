package bg.emanuil.presentation.lower_thirds_song

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LowerThirdsSong(
    viewModel: LowerThirdsSongViewModel = koinViewModel()
) {

    val text by viewModel.text.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center) {
        text?.let { text ->
            val textSize = TextAutoSize.StepBased(
                minFontSize = 36.sp,
                maxFontSize = 76.sp,
                stepSize = 1.sp
            )
            BasicText(
                style = TextStyle(
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    drawStyle = Stroke(
                        width = 15f,
                        miter = 2f,
                        join = StrokeJoin.Round
                    )
                ),
                text = text,
                maxLines = 2,
                autoSize = textSize
            )

            BasicText(
                style = TextStyle(
                    color = Color.White,
                    textAlign = TextAlign.Center,
                ),
                text = text,
                maxLines = 2,
                autoSize = textSize
            )
        }
    }
}