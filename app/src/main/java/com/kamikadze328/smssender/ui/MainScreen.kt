package com.kamikadze328.smssender.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kamikadze328.smssender.MainUiEvent
import com.kamikadze328.smssender.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ), title = {
                Text("Top app bar")
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onEvent(MainUiEvent.OnRefreshClicked) }) {
                Icon(Icons.Filled.Refresh, "")
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val state by viewModel.uiState.collectAsState()

            SmsScreen(
                modifier = Modifier.padding(8.dp),
                smsList = state.sms,
            )
        }
    }
}
