package com.example.courseworkandroidweeklyplanner.domain.interactor.builder

import android.util.Log
import com.example.courseworkandroidweeklyplanner.domain.Converter
import com.example.courseworkandroidweeklyplanner.domain.interactor.saver.TaskInteractor
import com.example.courseworkandroidweeklyplanner.domain.model.Difficulty
import com.example.courseworkandroidweeklyplanner.domain.model.Priority
import com.example.courseworkandroidweeklyplanner.domain.model.Task
import com.example.courseworkandroidweeklyplanner.domain.model.TaskSchema
import com.example.courseworkandroidweeklyplanner.domain.usecase.CheckDailyTaskLimitUseCase
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

    suspend fun validate(): TaskBuilderReport {
        val stateValue = _state.value
        if (stateValue !is TaskBuilderState.Default) {
            return TaskBuilderReport.NotInitialized
        }

        val task = stateValue.task

        // 1) Проверяем имя
        val nameReport = when {
            task.name.isBlank() -> NameReport.Empty
            task.name.length > 100 -> NameReport.TooLong
            task.name.trim() != task.name -> NameReport.UselessWhitespaces
            else -> NameReport.Valid
        }

        // 2) Проверяем лимит 1-3-5, но только если имя валидно
        val taskLimitReport = if (nameReport == NameReport.Valid) {
            Log.d("MSWORK6", taskId.toString())
            val canAdd = checkDailyTaskLimitUseCase(task.date, task.difficulty, taskId)
            if (canAdd) TaskLimitReport.Valid else TaskLimitReport.Exceeded
        } else {
            // если имя невалидно, нам не важно, сколько там задач
            TaskLimitReport.Valid
        }

        return TaskBuilderReport.Default(
            nameReport       = nameReport,
            taskLimitReport  = taskLimitReport
        )
    }

    suspend fun save() {
        val taskState = _state.value
        if (taskState is TaskBuilderState.Default){
            val task = taskState.task

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
