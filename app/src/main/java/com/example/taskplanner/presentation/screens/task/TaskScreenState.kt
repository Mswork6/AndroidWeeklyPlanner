package com.example.taskplanner.presentation.screens.task

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.example.taskplanner.domain.model.Category
import com.example.taskplanner.domain.model.Difficulty
import com.example.taskplanner.domain.model.NotificationTime
import com.example.taskplanner.domain.model.Priority
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
        val difficulty: Difficulty
        val category: Category
        val time: LocalTime
        val notificationTimeOffsetEnum: NotificationTime?
        val notificationTimeOffset: Long?
        val isDatePickerOpened: Boolean
        val isPriorityPickerOpened: Boolean
        val isDifficultyPickerOpened: Boolean
        val isCategoryPickerOpened: Boolean
        val isTimePickerOpened: Boolean
        val isNotificationTimePickerOpened: Boolean
        val isTaskLimitWindowOpened: Boolean
        @get:StringRes
        val nameErrorMessage: Int?
        val descriptionErrorMessage: Int?
        val editable: Boolean
    }

    @Immutable
    data class View(
        override val name: String,
        override val description: String?,
        override val date: LocalDate,
        override val priority: Priority,
        override val difficulty: Difficulty,
        override val category: Category,
        override val time: LocalTime,
        override val notificationTimeOffset: Long?
    ) : Content {
        override val isDatePickerOpened: Boolean
            get() = false
        override val isPriorityPickerOpened: Boolean
            get() = false
        override val isDifficultyPickerOpened: Boolean
            get() = false
        override val isCategoryPickerOpened: Boolean
            get() = false
        override val isTimePickerOpened: Boolean
            get() = false
        override val notificationTimeOffsetEnum: NotificationTime
            get() = NotificationTime.NONE
        override val isNotificationTimePickerOpened: Boolean
            get() = false
        override val isTaskLimitWindowOpened: Boolean
            get() = false
        override val nameErrorMessage: Int?
            get() = null
        override val descriptionErrorMessage: Int?
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
        override val difficulty: Difficulty,
        override val category: Category,
        override val time: LocalTime,
        override val notificationTimeOffsetEnum: NotificationTime,
        override val notificationTimeOffset: Long?,
        override val isPriorityPickerOpened: Boolean,
        override val isDifficultyPickerOpened: Boolean,
        override val isCategoryPickerOpened: Boolean,
        override val isDatePickerOpened: Boolean,
        override val isTimePickerOpened: Boolean,
        override val isNotificationTimePickerOpened: Boolean,
        override val isTaskLimitWindowOpened: Boolean,
        override val nameErrorMessage: Int?,
        override val descriptionErrorMessage: Int?
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
        override val difficulty: Difficulty,
        override val category: Category,
        override val time: LocalTime,
        override val notificationTimeOffsetEnum: NotificationTime?,
        override val notificationTimeOffset: Long?,
        override val isPriorityPickerOpened: Boolean,
        override val isDifficultyPickerOpened: Boolean,
        override val isCategoryPickerOpened: Boolean,
        override val isDatePickerOpened: Boolean,
        override val isTimePickerOpened: Boolean,
        override val isNotificationTimePickerOpened: Boolean,
        override val isTaskLimitWindowOpened: Boolean,
        override val nameErrorMessage: Int?,
        override val descriptionErrorMessage: Int?
    ) : Content {
        override val editable: Boolean
            get() = true
    }
}
