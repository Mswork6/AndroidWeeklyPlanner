package com.example.taskplanner.domain.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class Week private constructor(
    val start: LocalDate,
    val end: LocalDate
) : Iterable<Pair<LocalDate, DayType>> {
    companion object {
        fun of(date: LocalDate): Week {
            val firstDayOfWeek = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val lastDayOfWeek = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
            return Week(start = firstDayOfWeek, end = lastDayOfWeek)
        }
    }

    fun previous(): Week {
        return of(this.start.minusWeeks(1))
    }

    fun next(): Week {
        return of(this.start.plusWeeks(1))
    }

    override fun iterator(): Iterator<Pair<LocalDate, DayType>> = iterator {
        var current = start
        var typeIndex = 0
        while (!current.isAfter(end)) {
            yield(Pair(current, DayType.entries[typeIndex]))
            current = current.plusDays(1)
            typeIndex += 1
        }
    }
}
