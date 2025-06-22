package com.example.taskplanner.domain.repository.impl
import com.example.taskplanner.domain.model.FilterConfig
import com.example.taskplanner.domain.repository.FilterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterRepositoryImpl @Inject constructor() : FilterRepository {
    private val _config = MutableStateFlow(FilterConfig())
    override fun getFilterConfig(): StateFlow<FilterConfig> = _config

    override suspend fun setFilterConfig(config: FilterConfig) {
        _config.update {
            it.copy(
                startDate = config.startDate,
                endDate = config.endDate,
                priorityFilter = config.priorityFilter,
                difficultyFilter = config.difficultyFilter)
        }
    }
}