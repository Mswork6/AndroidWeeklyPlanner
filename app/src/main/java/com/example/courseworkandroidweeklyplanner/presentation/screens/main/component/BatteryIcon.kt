package com.example.courseworkandroidweeklyplanner.presentation.screens.main.component

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.courseworkandroidweeklyplanner.R
import com.example.courseworkandroidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme

@Composable
fun BatteryIcon(
    bodyCoefficient: Float = 1f,
    color: Color = Color.Black
) {
    Canvas(
        modifier = Modifier
            .size(24.dp, 12.dp)
    ) {
        val batteryContactWidth = 2.dp.toPx()
        val batteryContactHeight = 4.dp.toPx()
        val strokeWidth = 1.dp.toPx()
        val offsetValue = 2.dp.toPx()

        val batteryContourWidth = size.width -
                (batteryContactWidth)
        val batteryContourHeight = size.height

        val batteryContactOffset = Offset(
            batteryContourWidth,
            (batteryContourHeight - batteryContactHeight) / 2
        )

        val batteryBodyWidth = size.width -
                (offsetValue * 2) - batteryContactWidth
        val batterBodyHeight = size.height - (offsetValue * 2)
        val batteryBodyOffset = Offset(offsetValue, offsetValue)

        drawRect(
            color = color,
            style = Stroke(strokeWidth),
            size = Size(
                batteryContourWidth,
                batteryContourHeight
            )
        )
        drawRect(
            topLeft = batteryContactOffset,
            size = Size(
                batteryContactWidth,
                batteryContactHeight
            ),
            color = color

        )
        drawRect(
            topLeft = batteryBodyOffset,
            size = Size(
                batteryBodyWidth * bodyCoefficient,
                batterBodyHeight
            ),
            color = color

        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BatteryIconPreview1() {
    CourseWorkAndroidWeeklyPlannerTheme {
        BatteryIcon(
            bodyCoefficient = 0.25f,
            color = colorResource(R.color.green)
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BatteryIconPreview2() {
    CourseWorkAndroidWeeklyPlannerTheme {
        BatteryIcon(
            bodyCoefficient = 0.5f,
            color = colorResource(R.color.orange)
            )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BatteryIconPreview3() {
    CourseWorkAndroidWeeklyPlannerTheme {
        BatteryIcon(
            color = colorResource(R.color.red)
        )
    }
}