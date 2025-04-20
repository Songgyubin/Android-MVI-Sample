package com.gyub.mvi_sample.presentation.detail.state

import com.gyub.mvi_sample.presentation.base.Reducer
import com.gyub.mvi_sample.presentation.detail.model.UserUiModel


sealed interface DetailSideEffect : Reducer.SideEffect {
    data object Load : DetailSideEffect

    data class ShowToastMessage(val messageId: Int) : DetailSideEffect

    data class LoadedUserDetail(val uiModel: UserUiModel) : DetailSideEffect

    data object ShowAvatarPreviewPopup : DetailSideEffect
}