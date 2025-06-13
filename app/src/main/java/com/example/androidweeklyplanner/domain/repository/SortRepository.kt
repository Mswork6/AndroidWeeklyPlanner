package com.example.androidweeklyplanner.domain.repository

import com.example.androidweeklyplanner.domain.model.SortConfig
import com.example.androidweeklyplanner.domain.model.SortType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SortRepository {
    fun getSortConfig(): StateFlow<SortConfig>

    suspend fun setSortConfig(config: SortConfig)
}