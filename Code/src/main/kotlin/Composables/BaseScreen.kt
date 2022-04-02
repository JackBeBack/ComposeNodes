package Composables

import Viewmodel.EventViewmodel
import Viewmodel.PositionViewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import java.io.File
import kotlin.random.Random

@Composable
fun BaseScreen(modifier: Modifier, screenSize: DpSize) {
    val positionViewModel by KoinJavaComponent.inject<PositionViewModel>(PositionViewModel::class.java)
    val eventViewModel by KoinJavaComponent.inject<EventViewmodel>(EventViewmodel::class.java)

    val canvaseTranslation = positionViewModel.trackNewOffset(0).collectAsState()
    val zoomLevel = eventViewModel.zoomLevel.collectAsState()
    val zoomModifier = Modifier.scale(1F)

    val button1 = positionViewModel.trackNewOffset(1, IntOffset(Random.nextInt(screenSize.width.value.toInt()), Random.nextInt(screenSize.height.value.toInt()))).collectAsState()
    val button2 = positionViewModel.trackNewOffset(2, IntOffset(Random.nextInt(screenSize.width.value.toInt()), Random.nextInt(screenSize.height.value.toInt()))).collectAsState()
    val button3 = positionViewModel.trackNewOffset(3, IntOffset(Random.nextInt(screenSize.width.value.toInt()), Random.nextInt(screenSize.height.value.toInt()))).collectAsState()
    val button4 = positionViewModel.trackNewOffset(4, IntOffset(Random.nextInt(screenSize.width.value.toInt()), Random.nextInt(screenSize.height.value.toInt()))).collectAsState()

    Box(modifier) {
        Canvas(modifier.background(Color.DarkGray).offset(offset = {canvaseTranslation.value}).rotate(0F).scale(zoomLevel.value)) {

            val image = useResource("backgroundTile2.png") { loadImageBitmap(it) }

            val paint = Paint().asFrameworkPaint().apply {
                isAntiAlias = true
                shader = ImageShader(image, TileMode.Repeated, TileMode.Repeated)
            }

            drawIntoCanvas {
                it.nativeCanvas.drawPaint(paint)
            }

            paint.reset()
        }

        OnNode(modifier = zoomModifier.offset(offset = {button1.value}), 1)

    }
}

@Composable
fun OnNode(modifier: Modifier, index: Int){
    val positionViewModel by KoinJavaComponent.inject<PositionViewModel>(PositionViewModel::class.java)

    val on = remember { mutableStateOf(true) }
    val text = remember { mutableStateOf("State = ${on.value}") }

    Box(modifier = modifier.size(100.dp, 200.dp).background(Color.LightGray).pointerInput(Unit) {
        forEachGesture {
            var start = Offset(0F, 0F)
            detectDragGestures(onDragStart = {s -> start = s}, onDragEnd = {}, onDrag = {change, dragAmount -> println(dragAmount)})
        }
    }){
        Text(text.value)
    }
}