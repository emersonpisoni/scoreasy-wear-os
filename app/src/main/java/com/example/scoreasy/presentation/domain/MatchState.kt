package com.example.scoreasy.presentation.domain

enum class Team { A, B }

data class MatchState(
    val pointsA: Int = 0,
    val pointsB: Int = 0,
    val history: List<MatchState> = emptyList()
)

sealed class Action {
    data class Point(val team: Team) : Action()
    object Undo : Action()
    object Reset : Action()
}

fun reduce(state: MatchState, action: Action): MatchState {
    fun withHistory(next: MatchState) = next.copy(history = state.history + state)

    return when (action) {
        is Action.Point -> {
            val next = when (action.team) {
                Team.A -> state.copy(pointsA = state.pointsA + 1)
                Team.B -> state.copy(pointsB = state.pointsB + 1)
            }
            withHistory(next)
        }
        Action.Undo -> state.history.lastOrNull()?.let { prev ->
            prev.copy(history = state.history.dropLast(1))
        } ?: state
        Action.Reset -> withHistory(MatchState())
    }
}
