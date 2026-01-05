package com.example.scoreasy.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*

import com.example.scoreasy.presentation.domain.Action
import com.example.scoreasy.presentation.domain.MatchState
import com.example.scoreasy.presentation.domain.Team
import com.example.scoreasy.presentation.domain.TennisPoint
@Composable
fun ScoreScreen(
    state: MatchState,
    onAction: (Action) -> Unit
) {
    Scaffold(
        timeText = { TimeText() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("ScorEasy", fontSize = 20.sp)
                Text("Games: ${state.gamesA}  -  ${state.gamesB}", fontSize = 18.sp)

                val pa = pointText(state.pointA, Team.A)
                val pb = pointText(state.pointB, Team.B)

                Text("Points: $pa  -  $pb", fontSize = 26.sp)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { onAction(Action.Point(Team.A)) }) { Text("+A") }
                Button(onClick = { onAction(Action.Point(Team.B)) }) { Text("+B") }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = { onAction(Action.Undo) }) { Text("Undo") }
                OutlinedButton(onClick = { onAction(Action.Reset) }) { Text("Reset") }
            }
        }
    }
}

private fun pointText(p: TennisPoint, forTeam: Team): String {
    return when (p) {
            TennisPoint.Love -> "0"
            TennisPoint.Fifteen -> "15"
            TennisPoint.Thirty -> "30"
            TennisPoint.Forty -> "40"
            TennisPoint.Deuce -> "40"
        is TennisPoint.Advantage -> if (p.team == forTeam) "AD" else "40"
    }
}
