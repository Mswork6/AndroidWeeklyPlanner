package com.example.taskplanner.domain.repository

import com.example.taskplanner.domain.model.FilterConfig
import kotlinx.coroutines.flow.StateFlow

interface FilterRepository {
    fun getFilterConfig(): StateFlow<FilterConfig>

    suspend fun setFilterConfig(config: FilterConfig)
}