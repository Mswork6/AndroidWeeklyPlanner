package com.example.androidweeklyplanner.presentation.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S, A> : ViewModel() {
    abstract val state: StateFlow<S>

    abstract suspend fun execute(action: A)

    fun handle(action: A) {
        viewModelScope.launch {
            execute(action)
        }
    }
}
