package com.example.scoreasy.presentation.domain

enum class Team { A, B }

/**
 * - Love = 0
 * - Fifteen = 15
 * - Thirty = 30
 * - Forty = 40
 * - Deuce = 40-40
 * - Advantage(A/B)
 */
sealed class TennisPoint {
    object Love : TennisPoint()
    object Fifteen : TennisPoint()
    object Thirty : TennisPoint()
    object Forty : TennisPoint()
    object Deuce : TennisPoint()
    data class Advantage(val team: Team) : TennisPoint()
}

data class MatchState(
    val gamesA: Int = 0,
    val gamesB: Int = 0,
    val pointA: TennisPoint = TennisPoint.Love,
    val pointB: TennisPoint = TennisPoint.Love,
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
        is Action.Point -> withHistory(applyPoint(state, action.team))
        Action.Undo -> state.history.lastOrNull()?.copy(history = state.history.dropLast(1)) ?: state
        Action.Reset -> withHistory(MatchState())
    }
}

private fun applyPoint(state: MatchState, winner: Team): MatchState {
    val a = state.pointA
    val b = state.pointB

    if (a is TennisPoint.Deuce || b is TennisPoint.Deuce) {
        return state.copy(
            pointA = if (winner == Team.A) TennisPoint.Advantage(Team.A) else TennisPoint.Forty,
            pointB = if (winner == Team.B) TennisPoint.Advantage(Team.B) else TennisPoint.Forty
        )
    }

    if (a is TennisPoint.Advantage || b is TennisPoint.Advantage) {
        val adv = (a as? TennisPoint.Advantage) ?: (b as TennisPoint.Advantage)
        return if (adv.team == winner) {
            winGame(state, winner)
        } else {
            state.copy(pointA = TennisPoint.Deuce, pointB = TennisPoint.Deuce)
        }
    }

    val nextA = if (winner == Team.A) nextNormalPoint(a) else a
    val nextB = if (winner == Team.B) nextNormalPoint(b) else b

    if (winner == Team.A && a is TennisPoint.Forty) {
        return if (b is TennisPoint.Forty) {
            state.copy(pointA = TennisPoint.Deuce, pointB = TennisPoint.Deuce)
        } else {
            winGame(state, Team.A)
        }
    }

    if (winner == Team.B && b is TennisPoint.Forty) {
        return if (a is TennisPoint.Forty) {
            state.copy(pointA = TennisPoint.Deuce, pointB = TennisPoint.Deuce)
        } else {
            winGame(state, Team.B)
        }
    }

    if (nextA is TennisPoint.Forty && nextB is TennisPoint.Forty) {
        return state.copy(pointA = TennisPoint.Deuce, pointB = TennisPoint.Deuce)
    }

    return state.copy(pointA = nextA, pointB = nextB)
}

private fun nextNormalPoint(p: TennisPoint): TennisPoint = when (p) {
    TennisPoint.Love -> TennisPoint.Fifteen
    TennisPoint.Fifteen -> TennisPoint.Thirty
    TennisPoint.Thirty -> TennisPoint.Forty
    TennisPoint.Forty -> TennisPoint.Forty
    TennisPoint.Deuce -> TennisPoint.Deuce
    is TennisPoint.Advantage -> p
}

private fun winGame(state: MatchState, winner: Team): MatchState {
    val newGamesA = state.gamesA + if (winner == Team.A) 1 else 0
    val newGamesB = state.gamesB + if (winner == Team.B) 1 else 0

    return state.copy(
        gamesA = newGamesA,
        gamesB = newGamesB,
        pointA = TennisPoint.Love,
        pointB = TennisPoint.Love
    )
}
