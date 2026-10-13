package com.kamikadze328.smssender.ui.icons

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
public val Close: ImageVector
    get() {
        if (_close != null) {
            return _close!!
        }
        _close =
            ImageVector.Builder(
                name = "close",
                defaultWidth = 20.dp,
                defaultHeight = 20.dp,
                viewportWidth = 20f,
                viewportHeight = 20f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.NonZero,
                    ) {
                        moveTo(6.06f, 15f)
                        lineTo(5f, 13.94f)
                        lineTo(8.94f, 10f)
                        lineTo(5f, 6.06f)
                        lineTo(6.06f, 5f)
                        lineTo(10f, 8.94f)
                        lineTo(13.94f, 5f)
                        lineTo(15f, 6.06f)
                        lineTo(11.06f, 10f)
                        lineTo(15f, 13.94f)
                        lineTo(13.94f, 15f)
                        lineTo(10f, 11.06f)
                        lineTo(6.06f, 15f)
                        close()
                    }
                }
                .build()
        return _close!!
    }

private var _close: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box {
        Icon(
            imageVector = Close,
            contentDescription = null,
            tint = Color.Black,
        )
    }
}