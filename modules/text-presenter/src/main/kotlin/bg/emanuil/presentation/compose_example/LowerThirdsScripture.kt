package bg.emanuil.presentation.compose_example

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.indication
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LowerThirdsScripture(
    viewModel: LowerThirdsScriptureViewModel = koinViewModel()
) {

    val title by viewModel.title.collectAsState()
    val body by viewModel.body.collectAsState()
    val textSize = TextAutoSize.StepBased(
        minFontSize = 25.sp,
        maxFontSize = 56.sp,
        stepSize = 1.sp
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(32.dp)) {
        body?.let { body ->
            Column(
                modifier = Modifier
                    .fillMaxSize()

                    .clip(RoundedCornerShape(8))
                    .background(Color.White.copy(alpha = 0.9f))
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start) {
                title?.let { title ->
                    Text(
                        text = title,
                        style = TextStyle(
                            color = Color(0xFFD99050),
                            fontWeight = FontWeight.Bold,
                            fontSize = 36.sp,
                            fontFamily = FontFamily.Serif
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp).fillMaxWidth())
                BasicText(
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.Serif
                    ),
                    text = body,
                    autoSize = textSize
                )
            }
        }
    }
}