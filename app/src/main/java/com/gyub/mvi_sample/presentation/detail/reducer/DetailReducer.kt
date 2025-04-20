package com.gyub.mvi_sample.presentation.detail.reducer

import com.gyub.mvi_sample.R
import com.gyub.mvi_sample.presentation.base.Reducer
import com.gyub.mvi_sample.presentation.detail.state.DetailEvent
import com.gyub.mvi_sample.presentation.detail.state.DetailSideEffect
import com.gyub.mvi_sample.presentation.detail.state.DetailUiState


class DetailReducer : Reducer<DetailUiState, DetailEvent, DetailSideEffect>() {
    override fun reduce(currentState: DetailUiState, event: DetailEvent): ReducerResult<DetailUiState, DetailSideEffect> {
        return when (event) {
            DetailEvent.LoadUserDetail -> {
                reducerResult(
                    newState = currentState,
                    sideEffects = DetailSideEffect.Load
                )
            }

            DetailEvent.LoadedUserDetailError -> {
                reducerResult(
                    newState = currentState.copy(
                        isLoading = false,
                        isError = true,
                        isSuccess = false
                    )
                )
            }

            DetailEvent.LoadedUserDetailLoading -> {
                reducerResult(
                    newState = currentState.copy(
                        isLoading = true,
                        isError = false,
                        isSuccess = false
                    )
                )
            }

            is DetailEvent.LoadedUserDetailSuccess -> {
                reducerResult(
                    newState = currentState.copy(
                        uiModel = event.uiModel,
                        isLoading = false,
                        isError = false,
                        isSuccess = true
                    )
                )
            }

            DetailEvent.ShowAvatarPreview -> {
                reducerResult(
                    newState = currentState,
                    sideEffects = listOf(
                        DetailSideEffect.ShowToastMessage(R.string.msg_zoom_image),
                        DetailSideEffect.ShowAvatarPreviewPopup
                    )
                )
            }
        }
    }
}