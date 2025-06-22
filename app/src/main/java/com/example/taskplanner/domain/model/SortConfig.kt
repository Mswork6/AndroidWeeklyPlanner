package com.example.taskplanner.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class SortConfig(
    val priorityOrder: SortType = SortType.STANDARD,
    val difficultyOrder: SortType = SortType.STANDARD
)
