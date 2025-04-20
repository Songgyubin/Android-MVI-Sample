package com.gyub.mvi_sample.presentation.main.state

import com.gyub.mvi_sample.presentation.base.Reducer

/**
 * 메인 화면 SideEffect
 */
sealed interface MainSideEffect : Reducer.SideEffect {

    data object Refresh : MainSideEffect

    data class ShowToastMessage(val messageId: Int) : MainSideEffect

    data class NavigateUserDetail(val userId: Int) : MainSideEffect

    data class ChangeSearchQuery(val query: String) : MainSideEffect
}