package com.example.taskplanner.domain.repository.impl

import com.example.taskplanner.domain.model.Week
import com.example.taskplanner.domain.repository.WeekRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeekRepositoryImpl @Inject constructor() : WeekRepository {
    private val weekStateInternal: MutableStateFlow<Week> = MutableStateFlow(Week.of(currentDate))

    override fun getWeek(): Flow<Week> = flow {
        weekStateInternal.collect {
            emit(it)
        }
    }

    override suspend fun setWeek(date: LocalDate) {
        weekStateInternal.emit(Week.of(date))
    }

    override fun setPreviousWeek() {
        weekStateInternal.update { week ->
            week.previous()
        }
    }

    override fun setNextWeek() {
        weekStateInternal.update { week ->
            week.next()
        }
    }

    private companion object {
        val currentDate: LocalDate
            get() = LocalDate.now()
    }
}
