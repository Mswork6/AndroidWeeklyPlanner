package com.example.courseworkandroidweeklyplanner.domain.interactor.builder

sealed interface TaskBuilderReport {
    data object NotInitialized : TaskBuilderReport

    data class Default(
        val nameReport: NameReport
    ) : TaskBuilderReport
}

sealed interface NameReport {
    data object Valid : NameReport

    data object Empty : NameReport

    data object TooLong : NameReport

    data object UselessWhitespaces : NameReport
}