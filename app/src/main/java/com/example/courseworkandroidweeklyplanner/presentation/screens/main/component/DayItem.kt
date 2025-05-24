package com.example.courseworkandroidweeklyplanner.presentation.screens.main.component

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.courseworkandroidweeklyplanner.R
import com.example.courseworkandroidweeklyplanner.domain.model.Day
import com.example.courseworkandroidweeklyplanner.domain.model.DayType.MONDAY
import com.example.courseworkandroidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme
import com.example.courseworkandroidweeklyplanner.presentation.dateToString
import com.example.courseworkandroidweeklyplanner.presentation.description
import java.time.LocalDate

@Composable
fun DayItem(
    day: Day,
    isExpanded: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rotationState by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = stringResource(R.string.descipition_dayitem_animation)
    )
    val allDone = enabled && day.tasks.all { it.isDone }

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("confetti.json"))

    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = allDone,
        iterations = 1,          // проиграть один раз
        speed = 1.0f
    )


    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .clipToBounds()
    ) {
        ItemCard(
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            onClick = {},
            contentModifier = Modifier
                .clickable(
                    enabled = enabled,
                    onClick = onClick
                )
                .padding(
                    top = 16.dp,
                    bottom = 16.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                modifier = Modifier
                    .weight(2f),
                text = stringResource(id = day.type.description),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                modifier = Modifier.weight(2f),
                text = dateToString(day.date),
            )
            if (enabled) {
                Icon(
                    modifier = Modifier
                        .weight(1f)
                        .rotate(rotationState),
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.description_show_hide_tasks)
                )
            } else {
                Row(
                    modifier = Modifier
                        .weight(1f)
                ) { }
            }

        }

        if (allDone && composition != null) {
            LottieAnimation(
                composition,
                progress,
                modifier = Modifier
                    .matchParentSize()
                    .clipToBounds(),    // на всю карточку
                contentScale = ContentScale.FillWidth,
                clipToCompositionBounds = false
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DayCardPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        val day = Day(
            id = 0,
            type = MONDAY,
            date = LocalDate.of(2024, 10, 8),
            tasks = listOf()
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DayItem(
                enabled = true,
                onClick = { },
                day = day,
                isExpanded = true,
                modifier = Modifier.fillMaxWidth()
            )
            DayItem(
                enabled = false,
                onClick = { },
                day = day,
                isExpanded = false,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
