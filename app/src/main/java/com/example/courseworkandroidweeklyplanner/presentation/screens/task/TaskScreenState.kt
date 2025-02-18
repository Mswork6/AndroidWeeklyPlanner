package com.example.courseworkandroidweeklyplanner.presentation.screens.task

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.example.courseworkandroidweeklyplanner.domain.model.Priority
import java.time.LocalDate
import java.time.LocalTime

sealed interface TaskScreenState {
    @Immutable
    data object Initial : TaskScreenState

    @Immutable
    data object Success : TaskScreenState

    @Immutable
    data object Error : TaskScreenState

    sealed interface Content : TaskScreenState {
        val name: String
        val description: String?
        val date: LocalDate
        val priority: Priority
        val time: LocalTime?
        val isDatePickerOpened: Boolean
        val isPriorityPickerOpened: Boolean
        val isTimePickerOpened: Boolean
        @get:StringRes
        val errorMessage: Int?
        val editable: Boolean
    }

    @Immutable
    data class View(
        override val name: String,
        override val description: String?,
        override val date: LocalDate,
        override val priority: Priority,
        override val time: LocalTime?
    ) : Content {
        override val isDatePickerOpened: Boolean
            get() = false
        override val isPriorityPickerOpened: Boolean
            get() = false
        override val isTimePickerOpened: Boolean
            get() = false
        override val errorMessage: Int?
            get() = null
        override val editable: Boolean
            get() = false
    }

    @Immutable
    data class Add(
        override val name: String,
        override val description: String?,
        override val date: LocalDate,
        override val priority: Priority,
        override val time: LocalTime?,
        override val isPriorityPickerOpened: Boolean,
        override val isDatePickerOpened: Boolean,
        override val isTimePickerOpened: Boolean,
        override val errorMessage: Int?
    ) : Content {
        override val editable: Boolean
            get() = true
    }

    @Immutable
    data class Edit(
        override val name: String,
        override val description: String?,
        override val date: LocalDate,
        override val priority: Priority,
        override val time: LocalTime?,
        override val isPriorityPickerOpened: Boolean,
        override val isDatePickerOpened: Boolean,
        override val isTimePickerOpened: Boolean,
        override val errorMessage: Int?
    ) : Content {
        override val editable: Boolean
            get() = true
    }
}
