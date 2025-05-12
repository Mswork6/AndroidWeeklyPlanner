package com.example.courseworkandroidweeklyplanner.presentation.screens.main.component

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.courseworkandroidweeklyplanner.R
import com.example.courseworkandroidweeklyplanner.domain.model.Day
import com.example.courseworkandroidweeklyplanner.domain.model.DayType.MONDAY
import com.example.courseworkandroidweeklyplanner.presentation.dateToString
import com.example.courseworkandroidweeklyplanner.presentation.core.CourseWorkAndroidWeeklyPlannerTheme
import com.example.courseworkandroidweeklyplanner.presentation.description
import java.time.LocalDate

@Composable
fun DayItem(
    day: Day,
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotationState by animateFloatAsState(
        targetValue = if(isExpanded) 180f else 0f,
        label = stringResource(R.string.descipition_dayitem_animation)
    )

    ItemCard(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        onClick = onClick,
        modifier = modifier,
        contentModifier = Modifier
            .padding(
                top = 16.dp,
                bottom = 16.dp,
                start = 8.dp,
                end = 8.dp),
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
        Icon(
            modifier = Modifier
                .weight(1f)
                .rotate(rotationState),
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = stringResource(R.string.description_show_hide_tasks)
        )
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
                onClick = { },
                day = day,
                isExpanded = true,
                modifier = Modifier.fillMaxWidth()
            )
            DayItem(
                onClick = { },
                day = day,
                isExpanded = false,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
