package com.kamikadze328.smssender.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.kamikadze328.smssender.R
import com.kamikadze328.smssender.SmsListUiEvent
import com.kamikadze328.smssender.SmsListUiState
import com.kamikadze328.smssender.SmsListViewModel
import com.kamikadze328.smssender.ui.icons.RefreshFilled
import com.kamikadze328.smssender.ui.theme.SmsSenderTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import java.util.Date

@Composable
fun SmsListScreenUi(
    viewModel: SmsListViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    SmsListScreenUi(
        uiState = uiState,
        onEvent = remember { viewModel::onEvent },
    )
}

@Composable
private fun SmsListScreenUi(
    uiState: SmsListUiState,
    onEvent: (SmsListUiEvent) -> Unit,
) {
    Scaffold(
        topBar = { MyAppBar(smsList = uiState.list) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(SmsListUiEvent.OnRefreshClicked) }) {
                Icon(
                    imageVector = RefreshFilled,
                    contentDescription = "",
                )
            }
        },
    ) { innerPadding ->
        SmsListUi(
            modifier = Modifier.padding(innerPadding),
            smsList = uiState.list,
            isLoading = uiState.isLoading,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyAppBar(
    smsList: ImmutableList<SmsUi>
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            val notSentSmsCount by remember {
                derivedStateOf { smsList.count { !it.isSent } }
            }
            val text = if (notSentSmsCount == 0) {
                stringResource(id = R.string.all_sms_sent)
            } else {
                pluralStringResource(
                    id = R.plurals.sms_not_sent,
                    notSentSmsCount,
                    notSentSmsCount
                )
            }
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    )
}

@Preview
@Composable
private fun MainScreenPreviewUi() {
    SmsSenderTheme {
        SmsListScreenUi(
            uiState = SmsListUiState(
                isLoading = true,
                list = persistentListOf(
                    SmsUi(
                        id = 1L,
                        receiverName = "Receiver",
                        senderName = "Sender",
                        text = "Text",
                        isSent = true,
                        dateTime = Date().toString(),
                    ),
                    SmsUi(
                        id = 2L,
                        receiverName = "Receiver123",
                        senderName = "Sende123 r",
                        text = "T 123 12ext",
                        isSent = false,
                        dateTime = Date().toString(),
                    ),
                    SmsUi(
                        id = 3L,
                        receiverName = "Receiver",
                        senderName = "Sender",
                        text = "Text",
                        isSent = true,
                        dateTime = Date().toString(),
                    )
                )
            ),
            onEvent = {}
        )
    }
}

