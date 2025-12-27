package com.example.scoreasy.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.scoreasy.presentation.domain.MatchState
import com.example.scoreasy.presentation.domain.Action
import com.example.scoreasy.presentation.domain.reduce
import com.example.scoreasy.presentation.ui.ScoreScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var state by remember { mutableStateOf(MatchState()) }

            ScoreScreen(
                state = state,
                onAction = { action ->
                    state = reduce(state, action)
                }
            )
        }
    }
}
