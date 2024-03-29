package com.lvs.chatgpt.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<Event : UiEvent, State : UiState> : ViewModel() {

    protected abstract val tag: String

    // Create Initial State of View
    private val initialState: State by lazy { createInitialState() }
    abstract fun createInitialState(): State

    // Get Current State
    val currentState: State
        get() = uiState.value

    private val _uiState: MutableStateFlow<State> = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event = _event.asSharedFlow()

    init {
        subscribeEvents()
    }

    /**
     * Start listening to Event
     */
    private fun subscribeEvents() {
        viewModelScope.launch {
            event.collect {
                handleEvent(it)
            }
        }
    }

    /**
     * Handle each event
     */
    protected abstract fun handleEvent(event: Event)

    /**
     * Set new Event
     */
    fun setEvent(event: Event) {
        val newEvent = event
        viewModelScope.launch { _event.emit(newEvent) }
    }


    /**
     * Set new Ui State
     */
    protected fun setState(reduce: State.() -> State) {
        val newState = currentState.reduce()
        _uiState.value = newState
    }

    open protected val exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Log.e(tag, exception.stackTraceToString())
    }

    protected fun launchOnBackground(
        exceptionHandler: CoroutineExceptionHandler = this.exceptionHandler,
        block: suspend CoroutineScope.() -> Unit,
    ): Job = viewModelScope.launch(context = exceptionHandler + Dispatchers.IO, block = block)

    protected fun asyncOnBackground(
        exceptionHandler: CoroutineExceptionHandler = this.exceptionHandler,
        block: suspend CoroutineScope.() -> Unit
    ): Deferred<Unit> = viewModelScope.async(context = exceptionHandler + Dispatchers.IO, block = block)


}


interface UiState

interface UiEvent

interface UiEffect