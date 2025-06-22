package com.example.taskplanner.domain.repository.impl

import com.example.taskplanner.domain.model.SortConfig
import com.example.taskplanner.domain.repository.SortRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SortRepositoryImpl @Inject constructor() : SortRepository {
    private val _config = MutableStateFlow(SortConfig())
    override fun getSortConfig(): StateFlow<SortConfig> = _config

    override suspend fun setSortConfig(config: SortConfig) {
        _config.update {
            it.copy(
                priorityOrder = config.priorityOrder,
                difficultyOrder = config.difficultyOrder
            )
        }
    }
}