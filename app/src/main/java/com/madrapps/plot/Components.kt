package com.madrapps.plot

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class DataPoint(val x: Float, val y: Float)

data class LinePlot(
    val lines: List<Line>,
    val grid: Grid? = null,
    val dragSelection: Connection? = Connection(
        Color.Red,
        strokeWidth = 2.dp,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(40f,20f))
    ),
) {
    data class Line(
        val dataPoints: List<DataPoint>,
        val connection: Connection?,
        val intersection: Intersection?,
        val highlight: Intersection? = null,
        val areaUnderLine: AreaUnderLine? = null
    )

    data class Connection(
        val color: Color = Color.Black,
        val strokeWidth: Dp = 3.dp,
        val cap: StrokeCap = Stroke.DefaultCap,
        val pathEffect: PathEffect? = null,
        /*FloatRange(from = 0.0, to = 1.0)*/
        val alpha: Float = 1.0f,
        val colorFilter: ColorFilter? = null,
        val blendMode: BlendMode = DrawScope.DefaultBlendMode,
        val draw: DrawScope.(Offset, Offset) -> Unit = { start, end ->
            drawLine(
                color,
                start,
                end,
                strokeWidth.toPx(),
                cap,
                pathEffect,
                alpha,
                colorFilter,
                blendMode
            )
        }
    )

    data class Intersection(
        val color: Color = Color.Black,
        val radius: Dp = 6.dp,
        /*@FloatRange(from = 0.0, to = 1.0)*/
        val alpha: Float = 1.0f,
        val style: DrawStyle = Fill,
        val colorFilter: ColorFilter? = null,
        val blendMode: BlendMode = DrawScope.DefaultBlendMode,
        val draw: DrawScope.(Offset) -> Unit = { center ->
            drawCircle(
                color,
                radius.toPx(),
                center,
                alpha,
                style,
                colorFilter,
                blendMode
            )
        }
    )

    data class AreaUnderLine(
        val color: Color = Color.LightGray,
        /*@FloatRange(from = 0.0, to = 1.0)*/
        val alpha: Float = 1.0f,
        val style: DrawStyle = Fill,
        val colorFilter: ColorFilter? = null,
        val blendMode: BlendMode = DrawScope.DefaultBlendMode,
        val draw: DrawScope.(Path) -> Unit = { path ->
            drawPath(path, color, alpha, style, colorFilter, blendMode)
        }
    )

    data class Grid(
        val color: Color,
        val steps: Int = 5,
        val lineWidth: Dp = 1.dp,
        val draw: DrawScope.(Rect, Float, Float) -> Unit = { region, xOffset, yOffset ->
            val (left, top, right, bottom) = region
            (0 until steps).forEach {
                val y = it * 25f
                val y1 = bottom - (y * yOffset)
                drawLine(
                    color,
                    Offset(left, y1),
                    Offset(right, y1),
                    lineWidth.toPx()
                )
            }
        }
    )
}