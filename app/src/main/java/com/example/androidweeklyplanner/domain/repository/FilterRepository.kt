package com.example.androidweeklyplanner.domain.repository

import com.example.androidweeklyplanner.domain.model.FilterConfig
import kotlinx.coroutines.flow.StateFlow

interface FilterRepository {
    fun getFilterConfig(): StateFlow<FilterConfig>

    suspend fun setFilterConfig(config: FilterConfig)
}