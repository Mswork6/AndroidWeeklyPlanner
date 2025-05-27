package com.example.androidweeklyplanner.presentation.screens.main.component

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplanner.R
import com.example.androidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme

@Composable
fun DifficultyCircle(
    color: Color
) {
    Canvas(
        modifier = Modifier
            .size(24.dp, 24.dp)
    ) {

        drawCircle(
            radius = size.minDimension/2f,
            color = color
        )

    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DifficultyCirclePreview1() {
    CourseWorkAndroidWeeklyPlannerTheme {
        DifficultyCircle(
            color = colorResource(R.color.green)
        )
    }
}

