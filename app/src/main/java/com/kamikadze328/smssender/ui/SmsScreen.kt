package com.kamikadze328.smssender.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.kamikadze328.smssender.R
import com.kamikadze328.smssender.ui.provider.SmsListPreviewParameterProvider
import com.kamikadze328.smssender.ui.provider.SmsPreviewParameterProvider

@Composable
fun SmsScreen(
    modifier: Modifier = Modifier,
    smsList: SmsList,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp),

        content = {
            items(smsList.list) {
                MessageCard(sms = it)
            }
        }
    )
}

@Preview
@Composable
fun SmsScreenPreview(
    @PreviewParameter(provider = SmsListPreviewParameterProvider::class, limit = 1)
    smsList: SmsList,
) {
    SmsScreen(
        smsList = smsList,
    )
}

@Preview(showBackground = true)
@Composable
fun MessageCard(
    @PreviewParameter(SmsPreviewParameterProvider::class)
    sms: SmsUi
) {
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
    ) {
        var isExpanded by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .fillMaxWidth()
        ) {
            MessageHeader(
                sms = sms,
                isExpanded = isExpanded,
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )
            MessageBody(
                sms = sms,
                isExpanded = isExpanded,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageHeader(
    @PreviewParameter(SmsPreviewParameterProvider::class)
    sms: SmsUi,
    isExpanded: Boolean = true,
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val text = if (isExpanded) {
                "${sms.senderName}; ${stringResource(id = R.string.sms_to)} ${sms.receiverName}"
            } else {
                sms.senderName
            }
            Text(
                modifier = Modifier.weight(1f),
                text = text,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            val checkIconColor = if (sms.isSent) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.tertiary
            }
            Image(
                modifier = Modifier.padding(start = 8.dp),
                imageVector = Icons.Outlined.Check,
                contentDescription = "",
                colorFilter = ColorFilter.tint(checkIconColor),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageBody(
    @PreviewParameter(SmsPreviewParameterProvider::class)
    sms: SmsUi,
    isExpanded: Boolean = true,
) {
    val surfaceColor = if (isExpanded) {
        MaterialTheme.colorScheme.tertiary
    } else {
        MaterialTheme.colorScheme.surface
    }
    val textColor =  if (isExpanded) {
        MaterialTheme.colorScheme.onTertiary
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    Surface(
        modifier = Modifier
            .padding(1.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 1.dp,
        color = surfaceColor,
    ) {
        val text = remember {
            buildString {
                appendLine(sms.text)
                appendLine()
                append(sms.dateTime)
            }
        }
        Text(
            text = text,
            modifier = Modifier.padding(all = 8.dp),
            color = textColor,
            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

