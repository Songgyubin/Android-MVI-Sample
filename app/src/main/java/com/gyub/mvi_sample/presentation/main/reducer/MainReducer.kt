package com.gyub.mvi_sample.presentation.main.reducer

import com.gyub.mvi_sample.presentation.base.Reducer
import com.gyub.mvi_sample.presentation.main.state.MainEvent
import com.gyub.mvi_sample.presentation.main.state.MainSideEffect
import com.gyub.mvi_sample.presentation.main.state.MainUiState

/**
 * Main Reducer
 */
class MainReducer : Reducer<MainUiState, MainEvent, MainSideEffect>() {
    override fun reduce(currentState: MainUiState, event: MainEvent): ReducerResult<MainUiState, MainSideEffect> {
        return when (event) {
            is MainEvent.ChangeSearchQuery -> {
                reducerResult(
                    newState = currentState.copy(searchQuery = event.newQuery),
                    sideEffects = MainSideEffect.ChangeSearchQuery(event.newQuery)
                )
            }

            is MainEvent.ShowUserDetail -> {
                reducerResult(
                    newState = currentState,
                    sideEffects = MainSideEffect.NavigateUserDetail(event.userId)
                )
            }

            MainEvent.Refresh -> {
                reducerResult(
                    newState = currentState,
                    sideEffects = MainSideEffect.Refresh
                )
            }

            MainEvent.DeleteSearchQuery -> {
                reducerResult(
                    newState = currentState.copy(searchQuery = ""),
                    sideEffects = listOf(
                        MainSideEffect.ChangeSearchQuery(query = ""),
                        MainSideEffect.Refresh
                    )
                )
            }
        }
    }
}