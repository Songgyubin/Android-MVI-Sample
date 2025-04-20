package com.gyub.mvi_sample.presentation.detail.state

import com.gyub.mvi_sample.presentation.base.Reducer
import com.gyub.mvi_sample.presentation.detail.model.UserUiModel


data class DetailUiState(
    val uiModel: UserUiModel = UserUiModel(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false
) : Reducer.State