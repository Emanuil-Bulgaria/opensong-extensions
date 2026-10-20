package bg.emanuil.presentation.lower_thirds_scripture

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LowerThirdsScripture(
    viewModel: LowerThirdsScriptureViewModel = koinViewModel()
) {

    val title by viewModel.title.collectAsState()
    val body by viewModel.body.collectAsState()
    LowerThirdsScriptureInternal(title, body)
}

@Composable
fun LowerThirdsScriptureInternal(title: String?, body: String?) {
    val textSize = TextAutoSize.StepBased(
        minFontSize = 25.sp,
        maxFontSize = 46.sp,
        stepSize = 1.sp
    )

    var lastBody by remember { mutableStateOf(body) }
    if(body != null) lastBody = body

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(32.dp)) {
        AnimatedVisibility(
            body != null,
            enter = slideInHorizontally(
                animationSpec = tween(durationMillis = 500, easing = LinearEasing),
                initialOffsetX = { fullWidth -> -fullWidth }
            ) + fadeIn(
                animationSpec = tween(durationMillis = 500, easing = LinearEasing)
            ),
            exit = slideOutHorizontally(
                animationSpec = tween(durationMillis = 500, easing = LinearEasing),
                targetOffsetX = { fullWidth -> -fullWidth }
            ) + fadeOut(
                animationSpec = tween(durationMillis = 500, easing = LinearEasing)
            )
        ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()

                        .clip(RoundedCornerShape(8))
                        .background(Color.White.copy(alpha = 0.9f))
                        .padding(16.dp)
                        .padding(top = 8.dp, start = 8.dp),
                    horizontalAlignment = Alignment.Start) {
                    title?.let { title ->
                        Text(
                            text = title,
                            style = TextStyle(
                                color = Color(0xFFD99050),
                                fontWeight = FontWeight.Bold,
                                fontSize = 40.sp,
                                fontFamily = FontFamily.SansSerif,
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.1f),
                                    offset = Offset.Zero,
                                    blurRadius = 4f
                                )
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp).fillMaxWidth())
                    AnimatedContent(
                        targetState = lastBody,
                        transitionSpec = {
                            val enter = slideInHorizontally(tween(
                                300, delayMillis = 300
                            )) { -it / 2} + fadeIn(tween(
                                300, delayMillis = 300
                            ))

                            val exit = slideOutHorizontally(tween(
                                300
                            )) { it / 2 } + fadeOut(tween(
                                300
                            ))

                            enter togetherWith exit
                        },
                        label = "bodyText"
                    ) { body ->
                        BasicText(
                            style = TextStyle(
                                color = Color.Black,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.SemiBold
                            ),
                            text = body.orEmpty(),
                            autoSize = textSize
                        )
                    }
                }
        }
    }
}