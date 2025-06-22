package com.example.androidweeklyplanner.domain.interactor.builder

sealed interface TaskBuilderReport {
    data object NotInitialized : TaskBuilderReport

    data class Default(
        val nameReport: NameReport,
        val descriptionReport: DescriptionReport,
        val taskLimitReport: TaskLimitReport
    ) : TaskBuilderReport
}

sealed interface NameReport {
    data object Valid : NameReport

    data object Empty : NameReport

    data object TooLong : NameReport
}

sealed interface DescriptionReport {
    data object Valid : DescriptionReport

    data object TooLong : DescriptionReport
}

sealed interface TaskLimitReport {
    data object Valid : TaskLimitReport

    data object Exceeded : TaskLimitReport
}