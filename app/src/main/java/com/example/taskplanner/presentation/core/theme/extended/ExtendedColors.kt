package com.example.taskplanner.presentation.core.theme.extended

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.example.taskplanner.presentation.core.theme.Black
import com.example.taskplanner.presentation.core.theme.DarkGreen
import com.example.taskplanner.presentation.core.theme.Gray
import com.example.taskplanner.presentation.core.theme.Green
import com.example.taskplanner.presentation.core.theme.LightGray
import com.example.taskplanner.presentation.core.theme.LightGreen
import com.example.taskplanner.presentation.core.theme.Orange
import com.example.taskplanner.presentation.core.theme.Red
import com.example.taskplanner.presentation.core.theme.White

@Immutable
data class ExtendedColors(
    val gray: Color = Gray,
    val lightGreen: Color = LightGreen,
    val green: Color = Green,
    val darkGreen: Color = DarkGreen,
    val orange: Color = Orange,
    val red: Color = Red,
    val lightGray: Color = LightGray,
    val white: Color = White,
    val black: Color = Black,
)