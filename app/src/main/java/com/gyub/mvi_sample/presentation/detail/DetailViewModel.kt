package com.gyub.mvi_sample.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gyub.mvi_sample.data.util.Result
import com.gyub.mvi_sample.domain.usecase.GetUserDetailUseCase
import com.gyub.mvi_sample.presentation.detail.model.toUiModel
import com.gyub.mvi_sample.presentation.detail.reducer.DetailReducer
import com.gyub.mvi_sample.presentation.detail.state.DetailEvent
import com.gyub.mvi_sample.presentation.detail.state.DetailSideEffect
import com.gyub.mvi_sample.presentation.detail.state.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserDetailUseCase: GetUserDetailUseCase
) : ViewModel() {
    private val userId = savedStateHandle.get<Int>(USER_ID) ?: 0

    private val reducer: DetailReducer by lazy { DetailReducer() }

    private val eventChannel = Channel<DetailEvent>(Channel.BUFFERED)
    private val _sideEffectChannel = Channel<DetailSideEffect>(Channel.BUFFERED)
    val sideEffectChannel = _sideEffectChannel.receiveAsFlow()

    val uiState: StateFlow<DetailUiState> = eventChannel
        .receiveAsFlow()
        .onStart {
            sendEvent(DetailEvent.LoadUserDetail)
        }
        .runningFold(DetailUiState(), ::reduceState)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DetailUiState()
        )


    fun loadUserDetail() {
        viewModelScope.launch {
            getUserDetailUseCase(userId).collect { result ->
                when (result) {
                    is Result.Error -> {
                        sendEvent(DetailEvent.LoadedUserDetailError)
                    }

                    Result.Loading -> {
                        sendEvent(DetailEvent.LoadedUserDetailLoading)
                    }

                    is Result.Success -> {
                        sendEvent(DetailEvent.LoadedUserDetailSuccess(result.data.toUiModel()))
                    }
                }
            }
        }
    }

    fun sendEvent(intent: DetailEvent) {
        viewModelScope.launch {
            eventChannel.send(intent)
        }
    }

    private fun reduceState(currentState: DetailUiState, intent: DetailEvent): DetailUiState {
        val (newState, sideEffects) = reducer.reduce(currentState, intent)
        sideEffects.forEach { sendSideEffect(it) }

        return newState
    }

    private fun sendSideEffect(effect: DetailSideEffect) {
        viewModelScope.launch {
            _sideEffectChannel.send(effect)
        }
    }


    companion object {
        private const val USER_ID = "id"
    }
}