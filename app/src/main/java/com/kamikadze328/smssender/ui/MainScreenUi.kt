package com.kamikadze328.smssender.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kamikadze328.smssender.MainUiEvent
import com.kamikadze328.smssender.MainViewModel
import com.kamikadze328.smssender.MainViewState
import com.kamikadze328.smssender.R
import com.kamikadze328.smssender.ui.theme.MyTheme
import kotlinx.collections.immutable.persistentListOf
import java.util.Date

@Composable
fun MainScreenUi(
    viewModel: MainViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    MainScreenUi(
        uiState = uiState,
        onEvent = remember { viewModel::onEvent },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenUi(
    uiState: MainViewState,
    onEvent: (MainUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(stringResource(id = R.string.app_name))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(MainUiEvent.OnRefreshClicked) }) {
                Icon(Icons.Filled.Refresh, "")
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SmsListUi(
                modifier = Modifier.padding(8.dp),
                smsList = uiState.sms,
            )
        }
    }
}

@Preview
@Composable
private fun MainScreenPreviewUi() {
    MyTheme {
        MainScreenUi(
            uiState = MainViewState(
                sms = SmsList(
                    persistentListOf(
                        SmsUi(
                            receiverName = "Receiver",
                            senderName = "Sender",
                            text = "Text",
                            isSent = true,
                            dateTime = Date().toString(),
                        ),
                        SmsUi(
                            receiverName = "Receiver123",
                            senderName = "Sende123 r",
                            text = "T 123 12ext",
                            isSent = false,
                            dateTime = Date().toString(),
                        ),
                        SmsUi(
                            receiverName = "Receiver",
                            senderName = "Sender",
                            text = "Text",
                            isSent = true,
                            dateTime = Date().toString(),
                        )
                    )
                ),
            ),
            onEvent = {}
        )
    }
}

