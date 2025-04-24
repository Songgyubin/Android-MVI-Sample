package com.gyub.mvi_sample.presentation.base

/**
 * Reducer
 * 현재 상태와 액션(또는 이벤트, 결과)을 받아
 * 새로운 상태와 수행할 사이드이펙트(sideEffects)를 생성하여 반환
 *
 * 생성된 사이드 이펙트는 ViewModel에서 관리
 *
 * @param State 상태
 * @param Event 이벤트, 결과
 * @param SideEffect 부수 효과
 */
abstract class Reducer<State : Reducer.State, Event : Reducer.Event, SideEffect : Reducer.SideEffect> {
    interface State
    interface Event
    interface SideEffect

    abstract fun reduce(currentState: State, event: Event): ReducerResult<State, SideEffect>

    data class ReducerResult<State : Reducer.State, SideEffect : Reducer.SideEffect>(val newState: State, val sideEffects: List<SideEffect>)

    /**
     * 사이드이펙트가 없는 상태 결과를 생성합니다.
     *
     * @param newState 이벤트 처리 후의 새로운 상태
     * @return ReducerResult - 사이드이펙트 없이 상태만 포함
     */
    fun <State : Reducer.State> reducerResult(
        newState: State,
    ): ReducerResult<State, SideEffect> = ReducerResult(newState, emptyList())

    /**
     * 하나의 사이드이펙트를 포함하는 상태 결과를 생성합니다.
     *
     * @param newState 이벤트 처리 후의 새로운 상태
     * @param sideEffects 수행할 하나의 사이드이펙트
     * @return ReducerResult - 상태와 하나의 사이드이펙트를 포함
     */
    fun <State : Reducer.State, SideEffect : Reducer.SideEffect> reducerResult(
        newState: State,
        sideEffects: SideEffect
    ): ReducerResult<State, SideEffect> = ReducerResult(newState, listOf(sideEffects))

    /**
     * 여러 개의 사이드이펙트를 포함하는 상태 결과를 생성합니다.
     *
     * @param newState 이벤트 처리 후의 새로운 상태
     * @param sideEffects 수행할 사이드이펙트 리스트
     * @return ReducerResult - 상태와 여러 사이드이펙트를 포함
     */
    fun <State : Reducer.State, SideEffect : Reducer.SideEffect> reducerResult(
        newState: State,
        sideEffects: List<SideEffect>
    ): ReducerResult<State, SideEffect> = ReducerResult(newState, sideEffects)
}