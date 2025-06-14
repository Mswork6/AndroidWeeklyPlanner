package com.example.androidweeklyplanner.domain.repository.impl

import com.example.androidweeklyplanner.domain.model.FilterConfig
import com.example.androidweeklyplanner.domain.repository.FilterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class FilterRepositoryImpl @Inject constructor() : FilterRepository {
    private val _config = MutableStateFlow(FilterConfig())

    override fun getFilterConfig(): StateFlow<FilterConfig> = _config

    override suspend fun setFilterConfig(config: FilterConfig) {
        _config.value = config
    }
}