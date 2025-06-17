package com.example.androidweeklyplanner.domain.repository.impl

import com.example.androidweeklyplanner.domain.model.SortConfig
import com.example.androidweeklyplanner.domain.model.SortType
import com.example.androidweeklyplanner.domain.repository.SortRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
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