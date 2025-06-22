package com.example.taskplanner.domain.repository

import com.example.taskplanner.domain.model.SortConfig
import kotlinx.coroutines.flow.StateFlow

interface SortRepository {
    fun getSortConfig(): StateFlow<SortConfig>

    suspend fun setSortConfig(config: SortConfig)
}