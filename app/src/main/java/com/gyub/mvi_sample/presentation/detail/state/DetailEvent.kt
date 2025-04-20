package com.gyub.mvi_sample.presentation.detail.state

import com.gyub.mvi_sample.presentation.base.Reducer
import com.gyub.mvi_sample.presentation.detail.model.UserUiModel


sealed interface DetailEvent : Reducer.Event {
    data object LoadUserDetail : DetailEvent

    data class LoadedUserDetailSuccess(val uiModel: UserUiModel) : DetailEvent

    data object LoadedUserDetailLoading : DetailEvent

    data object LoadedUserDetailError : DetailEvent

    data object ShowAvatarPreview : DetailEvent
}