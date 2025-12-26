package com.example.scoreasy.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*

import com.example.scoreasy.presentation.domain.Action
import com.example.scoreasy.presentation.domain.MatchState
import com.example.scoreasy.presentation.domain.Team

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
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("Placar", fontSize = 16.sp)
                Spacer(Modifier.height(6.dp))
                Text("${state.pointsA}  -  ${state.pointsB}", fontSize = 34.sp)
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
                OutlinedButton(onClick = { onAction(Action.Reset) }) { Text("Zerar") }
            }
        }
    }
}
