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
val DoneAllFilled: ImageVector
    get() {
        if (_done_all != null) {
            return _done_all!!
        }
        _done_all =
            ImageVector.Builder(
                name = "done_all",
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
                        pathFillType = PathFillType.Companion.NonZero,
                    ) {
                        moveTo(6.1f, 14f)
                        lineTo(2.08f, 9.96f)
                        lineTo(3.13f, 8.92f)
                        lineTo(6.1f, 11.88f)
                        lineToRelative(1.06f, 1.06f)
                        lineTo(6.1f, 14f)
                        close()
                        moveToRelative(4.25f, 0f)
                        lineTo(6.31f, 9.96f)
                        lineTo(7.38f, 8.9f)
                        lineToRelative(2.98f, 2.98f)
                        lineTo(17.1f, 5.13f)
                        lineToRelative(1.06f, 1.06f)
                        lineTo(10.35f, 14f)
                        close()
                        moveToRelative(0f, -4.23f)
                        lineTo(9.29f, 8.71f)
                        lineTo(12.88f, 5.13f)
                        lineToRelative(1.06f, 1.06f)
                        lineTo(10.35f, 9.77f)
                        close()
                    }
                }
                .build()
        return _done_all!!
    }

private var _done_all: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box {
        Icon(
            imageVector = DoneAllFilled,
            contentDescription = null,
            tint = Color.Black,
        )
    }
}