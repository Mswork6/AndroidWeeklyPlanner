package com.example.androidweeklyplanner.presentation.screens.shared

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.androidweeklyplanner.domain.model.SortType
import com.example.androidweeklyplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme
import com.example.androidweeklyplanner.presentation.icon

@SuppressLint("ResourceType")
@Composable
fun SortingChipGroup(
    title: String,
    height: Dp = 32.dp,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    selectedOrder: SortType,
    onOrderChange: (SortType) -> Unit,

) {
    Column {
        Text(
            text = title,
            style = textStyle
        )
        Row {
            SortType.entries.forEachIndexed { index, order ->

                val shape = when (index) {
                    0 -> RoundedCornerShape(
                        topStart = 16.dp,
                        bottomStart = 16.dp,
                        topEnd = 0.dp,
                        bottomEnd = 0.dp
                    )
                    SortType.entries.lastIndex -> RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 0.dp,
                        topEnd = 16.dp,
                        bottomEnd = 16.dp
                    )
                    else -> RectangleShape
                }

                FilterChip(
                    selected = order == selectedOrder,
                    onClick = { onOrderChange(order) },
                    modifier = Modifier
                        .sizeIn(minWidth = 32.dp, minHeight = height)
                        .weight(1f),
                    label = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(order.icon),
                                contentDescription = null
                            )
                        }
                    },
                    shape = shape,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SortingChipGroupPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        SortingChipGroup(
            title = "Title",
            selectedOrder = SortType.STANDARD,
            onOrderChange = {}


        )
    }
}