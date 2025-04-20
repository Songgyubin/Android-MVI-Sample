package com.gyub.mvi_sample.presentation.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.gyub.mvi_sample.data.model.GithubUser
import com.gyub.mvi_sample.domain.usecase.SearchUsersUseCase
import com.gyub.mvi_sample.presentation.main.reducer.MainReducer
import com.gyub.mvi_sample.presentation.main.state.MainEvent
import com.gyub.mvi_sample.presentation.main.state.MainSideEffect
import com.gyub.mvi_sample.presentation.main.state.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 메인 ViewModel
 */
@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val searchUsersUseCase: SearchUsersUseCase
) : ViewModel() {
    private val reducer: MainReducer by lazy { MainReducer() }

    private val eventChannel = Channel<MainEvent>(Channel.BUFFERED)

    private val _sideEffectChannel = Channel<MainSideEffect>(Channel.BUFFERED)
    val sideEffectChannel = _sideEffectChannel.receiveAsFlow()

    val uiState: StateFlow<MainUiState> = eventChannel
        .receiveAsFlow()
        .runningFold(
            MainUiState(
                searchQuery = savedStateHandle[SEARCH_QUERY] ?: ""
            ), ::reduceState
        )
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            MainUiState()
        )

    private val searchQueryFlow: StateFlow<String> =
        savedStateHandle.getStateFlow(SEARCH_QUERY, "")

    val users: Flow<PagingData<GithubUser>> = searchQueryFlow
        .onEach { sendEvent(MainEvent.ChangeSearchQuery(it)) }
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query -> searchUsersUseCase(query) }
        .cachedIn(viewModelScope)

    fun sendEvent(intent: MainEvent) {
        viewModelScope.launch {
            eventChannel.send(intent)
        }
    }

    fun saveSearchQuery(searchQuery: String) {
        savedStateHandle[SEARCH_QUERY] = searchQuery
    }

    private fun reduceState(currentState: MainUiState, intent: MainEvent): MainUiState {
        val (newState, sideEffects) = reducer.reduce(currentState, intent)
        sideEffects.forEach { sendSideEffect(it) }

        return newState
    }

    private fun sendSideEffect(effect: MainSideEffect) {
        viewModelScope.launch {
            _sideEffectChannel.send(effect)
        }
    }

    companion object {
        private const val SEARCH_QUERY = "searchQuery"
    }
}