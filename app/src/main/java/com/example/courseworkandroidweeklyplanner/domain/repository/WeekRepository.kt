package com.example.courseworkandroidweeklyplanner.domain.repository

import com.example.courseworkandroidweeklyplanner.domain.model.Week
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface WeekRepository {
    fun getWeek(): Flow<Week>

    suspend fun setWeek(date: LocalDate)

    fun setPreviousWeek()

    fun setNextWeek()
}