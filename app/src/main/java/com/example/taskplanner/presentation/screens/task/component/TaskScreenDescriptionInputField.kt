package com.example.taskplanner.presentation.screens.task.component

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskplanner.R
import com.example.taskplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme

@Composable
internal fun TaskScreenDescriptionInputField(
    text: String?,
    descriptionErrorString: String?,
    editState: Boolean,
    onTaskDescriptionValueChange: (String) -> Unit,
    modifier: Modifier
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.Start,
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    BasicTextField(
        value = text ?: "",
        readOnly = !editState,
        onValueChange = onTaskDescriptionValueChange,
        maxLines = 5,
        modifier = Modifier
            .fillMaxWidth()
            .sizeIn(minHeight = 100.dp)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(8.dp)
            ),
        decorationBox = { innerTextField ->
            Box(modifier = Modifier.padding(8.dp)) {
                if (text?.isEmpty() != false) {
                    Text(
                        text = stringResource(id = R.string.description_task_description),
                        color = Color.Gray
                    )
                }
                innerTextField()
            }
        }
    )

    if (descriptionErrorString != null) {
        Text(
            text = descriptionErrorString,
            color = Color.Red,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskAddScreenInputFieldPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        TaskScreenDescriptionInputField(
            text = "",
            descriptionErrorString = null,
            editState = false,
            onTaskDescriptionValueChange = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}