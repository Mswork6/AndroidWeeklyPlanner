package com.example.taskplanner.presentation.screens.shared

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskplanner.R
import com.example.taskplanner.presentation.core.theme.CourseWorkAndroidWeeklyPlannerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TopBar(
    actionName: String?,
    navigateBackAction: () -> Unit,
    confirmAction: () -> Unit,
    modifier: Modifier = Modifier,
) = TopAppBar(
    modifier = modifier,
    title = {},
    navigationIcon = {
        IconButton(onClick = navigateBackAction)
        {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(id = R.string.action_back)
            )
        }
    },
    actions = {
        if (actionName != null) {
            TextButton(onClick = confirmAction) {
                Text(
                    text = actionName,
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
)

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TaskAddScreenTopBarPreview() {
    CourseWorkAndroidWeeklyPlannerTheme {
        TopBar(
            actionName = "Execute",
            navigateBackAction = {},
            confirmAction = {},
        )
    }
}