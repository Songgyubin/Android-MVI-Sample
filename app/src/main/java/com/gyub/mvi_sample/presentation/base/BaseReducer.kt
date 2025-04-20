package com.gyub.mvi_sample.presentation.base

/**
 * Base Reducer
 * 현재 상태와 액션(또는 이벤트, 결과)을 받아
 * 새로운 상태를 생성하는 역할
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

    fun <State : Reducer.State> reducerResult(
        newState: State,
    ): ReducerResult<State, SideEffect> = ReducerResult(newState, emptyList())

    fun <State : Reducer.State, SideEffect : Reducer.SideEffect> reducerResult(
        newState: State,
        sideEffects: SideEffect
    ): ReducerResult<State, SideEffect> = ReducerResult(newState, listOf(sideEffects))

    fun <State : Reducer.State, SideEffect : Reducer.SideEffect> reducerResult(
        newState: State,
        sideEffects: List<SideEffect>
    ): ReducerResult<State, SideEffect> = ReducerResult(newState, sideEffects)
}