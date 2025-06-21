package com.example.androidweeklyplanner.domain.interactor.builder

import com.example.androidweeklyplanner.domain.Converter
import com.example.androidweeklyplanner.domain.interactor.saver.TaskInteractor
import com.example.androidweeklyplanner.domain.model.Category
import com.example.androidweeklyplanner.domain.model.Difficulty
import com.example.androidweeklyplanner.domain.model.NotificationTime
import com.example.androidweeklyplanner.domain.model.Priority
import com.example.androidweeklyplanner.domain.model.Task
import com.example.androidweeklyplanner.domain.model.TaskSchema
import com.example.androidweeklyplanner.domain.usecase.CheckDailyTaskLimitUseCase
import com.example.androidweeklyplanner.presentation.convertToLocalDate
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.UUID

class TaskBuilderInteractor @AssistedInject constructor(
    @Assisted private val taskId: UUID?,
    @Assisted private val scope: CoroutineScope,
    private val taskInteractor: TaskInteractor,
    private val checkDailyTaskLimitUseCase: CheckDailyTaskLimitUseCase
) : Converter<TaskSchema, Task> {
    private val _schemaState: MutableStateFlow<TaskSchema?> = MutableStateFlow(null)
    private val _state: MutableStateFlow<TaskBuilderState> =
        MutableStateFlow(TaskBuilderState.Initial)
    val state: Flow<TaskBuilderState> = _state.asStateFlow()

    init {
        // Проверяем переданный taskId, создаем схему задачи если задача существует
        scope.launch {
            when (taskId) {
                null -> _schemaState.emit(TaskSchema())
                else -> when (val task = taskInteractor.getTask(taskId)) {
                    null -> _state.emit(TaskBuilderState.Invalid)
                    else -> _schemaState.emit(convertToEntity(task))
                }
            }
        }
        scope.launch {
            _schemaState.collect { schema ->
                // Обновляем стейт интерактора при изменении схемы задачи
                _state.update {
                    when (it) {
                        is TaskBuilderState.Initial -> when (schema) {
                            null -> it
                            else -> TaskBuilderState.Default(convertToState(schema))
                        }

                        is TaskBuilderState.Default -> when (schema) {
                            null -> TaskBuilderState.Invalid
                            else -> it.copy(task = convertToState(schema))
                        }

                        TaskBuilderState.Invalid -> it
                    }
                }
            }
        }
    }

    fun setName(name: String) {
        _schemaState.update {
            it?.copy(name = name)
        }
    }

    fun setDescription(description: String?) {
        _schemaState.update {
            it?.copy(description = description)
        }
    }

    fun setPriority(priority: Priority) {
        _schemaState.update {
            it?.copy(priority = priority)
        }
    }

    fun setDifficulty(difficulty: Difficulty) {
        _schemaState.update {
            it?.copy(difficulty = difficulty)
        }
    }

    fun setCategory(category: Category) {
        _schemaState.update {
            it?.copy(category = category)
        }
    }

    fun setDate(dateInMillis: Long) {
        _schemaState.update {
            it?.copy(day = convertToLocalDate(dateInMillis))
        }
    }

    fun setTime(hour: Int, minute: Int) {
        _schemaState.update {
            it?.copy(
                time = LocalTime.of(hour, minute)
            )
        }
    }

    fun setNotificationTime(notificationTime: NotificationTime) {
        _schemaState.update {
            val timeOffset = notificationTime.offsetMinutes
            it?.copy(notificationTimeOffset = timeOffset)
        }
    }

    suspend fun validate(): TaskBuilderReport {
        val stateValue = _state.value
        if (stateValue !is TaskBuilderState.Default) {
            return TaskBuilderReport.NotInitialized
        }

        val task = stateValue.task

        val nameTrimmed = task.name.trim()
        val descriptionTrimmed = task.description?.trim()

        // Проверка имени
        val nameReport = when {
            nameTrimmed.isBlank() -> NameReport.Empty
            nameTrimmed.length > 100 -> NameReport.TooLong
            else -> NameReport.Valid
        }

        // Проверка описания
        val descriptionReport = when {
            descriptionTrimmed.isNullOrEmpty() -> DescriptionReport.Valid
            descriptionTrimmed.length > 300 -> DescriptionReport.TooLong
            else -> DescriptionReport.Valid
        }

        // Проверка лимита 1-3-5, но только если имя и описание валидно
        val taskLimitReport = if (nameReport == NameReport.Valid
            && descriptionReport == DescriptionReport.Valid) {
            val canAdd = checkDailyTaskLimitUseCase(task.date, task.difficulty, taskId)
            if (canAdd) TaskLimitReport.Valid else TaskLimitReport.Exceeded
        } else {
            // если невалидно, нам не важно, сколько там задач
            TaskLimitReport.Valid
        }

        return TaskBuilderReport.Default(
            nameReport = nameReport,
            descriptionReport = descriptionReport,
            taskLimitReport  = taskLimitReport
        )
    }

    suspend fun save() {
        val taskState = _state.value
        if (taskState is TaskBuilderState.Default){
            val task = taskState.task.copy(
                name = taskState.task.name.trim(),
                description = taskState.task.description?.trim()
            )


            when (taskId) {
                null -> taskInteractor.addTask(task)
                else -> taskInteractor.updateTask(task)
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(taskId: UUID?, scope: CoroutineScope): TaskBuilderInteractor
    }

    override fun convertToEntity(state: Task): TaskSchema {
        return with(state) {
            TaskSchema(
                id = id,
                name = name,
                description = description,
                priority = priority,
                difficulty = difficulty,
                category = category,
                day = date,
                time = time,
                notificationTimeOffset = notificationTimeOffset,
                isDone = isDone
            )
        }
    }

    override fun convertToState(entity: TaskSchema): Task {
        return with(entity) {
            Task(
                id = id ?: UUID.randomUUID(),
                name = name,
                description = description,
                date = day,
                time = time,
                notificationTimeOffset = notificationTimeOffset,
                priority = priority,
                difficulty = difficulty,
                category = category,
                isDone = isDone ?: false,
            )
        }
    }
}
