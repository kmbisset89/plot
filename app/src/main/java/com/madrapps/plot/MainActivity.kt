package com.madrapps.plot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.madrapps.plot.ui.theme.PlotTheme
import kotlin.system.measureTimeMillis

private val dataPoints = listOf(
    DataPoint(0f, 0f),
    DataPoint(1f, 0f),
    DataPoint(2f, 0f),
    DataPoint(3f, 0f),
    DataPoint(4f, 0f),
    DataPoint(5f, 25f),
    DataPoint(6f, 75f),
    DataPoint(7f, 100f),
    DataPoint(8f, 80f),
    DataPoint(9f, 75f),
    DataPoint(10f, 55f),
    DataPoint(11f, 45f),
    DataPoint(12f, 50f),
    DataPoint(13f, 80f),
    DataPoint(14f, 70f),
    DataPoint(15f, 25f),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlotTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    LineGraph(dataPoints)
                }
            }
        }
    }
}

@Composable
fun LineGraph(dataPoints: List<DataPoint>) {
    val pointRadius = 6.dp
    val lineWidth = 3.dp
    Canvas(modifier = Modifier
        .height(300.dp)
        .fillMaxWidth(),
        onDraw = {
            val xStart = 30.dp.toPx()
            val yStart = 30.dp.toPx()
            val availableWidth = (size.width - xStart)
            val availableHeight = size.height - yStart
            val xScale = 1f
            val yScale = 0.9f
            val xOffset = 30.dp.toPx()
            val yOffset = availableHeight / dataPoints.maxOf { it.y }

            var prevOffset: Offset? = null
            dataPoints.forEach { (x, y) ->
                val x1 = (x * xOffset*xScale) + xStart
                val y1 = availableHeight - (y * yOffset*yScale)
                val curOffset = Offset(x1, y1)
                drawCircle(Color.Blue, pointRadius.toPx(), curOffset)

                if (prevOffset != null) {
                    drawLine(Color.Blue, prevOffset!!, curOffset, lineWidth.toPx())
                }
                prevOffset = curOffset
            }
        })
}

@Composable
fun LineGraphSample(dataPoints: List<DataPoint>) {
    val color = remember { mutableStateOf(Color.Gray) }
    val offset = remember {
        mutableStateOf(Offset(0f, 0f))
    }
    val timeTaken = measureTimeMillis {
        Canvas(modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
//            detectTapGestures(
//                onDoubleTap = {
//                    color.value = Color.Red
//                },
//                onLongPress = { color.value = Color.Green },
//                onTap = { color.value = Color.Yellow },
//                onPress = {
//                    color.value = Color.Magenta
//                }
//            )
                detectDragGestures { change, _ ->
                    change.consumeAllChanges()
                    offset.value = change.position
                }
            }, onDraw = {
            drawCircle(color.value, radius = 100f, center = offset.value)
            val xStart = 200
            val yStart = 200
            val fX = (size.width - 2 * xStart) / dataPoints.size
            val height = size.height - 2 * yStart
            val fY = height / dataPoints.maxOf { it.y }

            var prevOffset: Offset? = null
            dataPoints.forEach { (x, y) ->
                val x1 = (x * fX) + xStart
                val y1 = height - (y * fY) + yStart
                val curOffset = Offset(x1, y1)
                drawCircle(Color.Blue, 10f, curOffset)

                if (prevOffset != null) {
                    drawLine(Color.Blue, prevOffset!!, curOffset, 5f)
                }
                prevOffset = curOffset
            }
        })
    }
    println("RONNY - TimeTaken = $timeTaken")

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlotTheme {
        LineGraph(dataPoints)
    }
}

data class DataPoint(val x: Float, val y: Float)