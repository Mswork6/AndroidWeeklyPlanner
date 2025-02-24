package com.example.courseworkandroidweeklyplanner.domain.interactor.builder

import com.example.courseworkandroidweeklyplanner.domain.Converter
import com.example.courseworkandroidweeklyplanner.domain.interactor.saver.TaskInteractor
import com.example.courseworkandroidweeklyplanner.domain.model.Priority
import com.example.courseworkandroidweeklyplanner.domain.model.Task
import com.example.courseworkandroidweeklyplanner.domain.model.TaskSchema
import com.example.courseworkandroidweeklyplanner.presentation.convertToLocalDate
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

    fun setDate(dateInMillis: Long) {
        _schemaState.update {
            it?.copy(day = convertToLocalDate(dateInMillis))
        }
    }

    fun setTime(hour: Int?, minute: Int?) {
        _schemaState.update {
            it?.copy(
                time = when {
                    hour != null && minute != null -> LocalTime.of(hour, minute)
                    else -> null
                }
            )
        }
    }

    suspend fun save(): TaskBuilderReport {
        return when (val taskState = _state.value) {
            is TaskBuilderState.Initial -> TaskBuilderReport.NotInitialized
            is TaskBuilderState.Invalid -> TaskBuilderReport.NotInitialized
            is TaskBuilderState.Default -> {
                val task = taskState.task
                when {
                    task.name.length > 100 -> TaskBuilderReport.Default(NameReport.TooLong)
                    task.name.isBlank() -> TaskBuilderReport.Default(NameReport.Empty)
                    task.name.trim() != task.name -> TaskBuilderReport.Default(NameReport.UselessWhitespaces)
                    else -> {
                        when (taskId) {
                            null -> taskInteractor.addTask(task)
                            else -> taskInteractor.updateTask(task)
                        }
                        TaskBuilderReport.Default(NameReport.Valid)
                    }
                }
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
                day = date,
                time = time,
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
                priority = priority,
                difficulty = difficulty,
                isDone = isDone ?: false,
            )
        }
    }
}
