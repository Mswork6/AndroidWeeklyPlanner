package com.example.androidweeklyplanner.domain.repository

import com.example.androidweeklyplanner.domain.model.SortType
import kotlinx.coroutines.flow.Flow

interface SortRepository {
    fun getSort(): Flow<SortType>

    suspend fun setSort(sortType: SortType)
}