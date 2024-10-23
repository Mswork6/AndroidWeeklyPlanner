package com.example.courseworkandroidweeklyplanner.domain.repository

import com.example.courseworkandroidweeklyplanner.domain.model.SortType
import kotlinx.coroutines.flow.Flow

interface SortRepository {
    fun getSort(): Flow<SortType>

    suspend fun setSort(sortType: SortType)
}