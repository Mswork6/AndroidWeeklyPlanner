package com.example.androidweeklyplanner.domain.repository.impl

import com.example.androidweeklyplanner.domain.model.SortType
import com.example.androidweeklyplanner.domain.repository.SortRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SortRepositoryImpl @Inject constructor() : SortRepository {
    private val sortStateInternal: MutableStateFlow<SortType> = MutableStateFlow(SortType.STANDARD)

    override fun getSort(): Flow<SortType> = flow {
        sortStateInternal.collect {
            emit(it)
        }
    }

    override suspend fun setSort(sortType: SortType) {
        sortStateInternal.emit(sortType)
    }
}