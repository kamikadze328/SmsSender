package com.kamikadze328.smssender.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.kamikadze328.smssender.R
import com.kamikadze328.smssender.ui.provider.SmsListPreviewParameterProvider
import com.kamikadze328.smssender.ui.provider.SmsPreviewParameterProvider
import com.kamikadze328.smssender.ui.theme.MyTheme

@Composable
internal fun SmsListUi(
    modifier: Modifier = Modifier,
    smsList: SmsList,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp),
    ) {
        items(smsList.list) {
            MessageCardUi(sms = it)
        }
    }
}

@Composable
private fun MessageCardUi(
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
            MessageHeaderUi(
                sms = sms,
                isExpanded = isExpanded,
            )
            MessageBodyUi(
                sms = sms,
                isExpanded = isExpanded,
            )
        }
    }
}

@Composable
private fun MessageHeaderUi(
    sms: SmsUi,
    isExpanded: Boolean = true,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
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
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            val iconBackgroundColor = if (sms.isSent) {
                MaterialTheme.colorScheme.background
            } else {
                MaterialTheme.colorScheme.errorContainer
            }
            val iconColor = if (sms.isSent) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onErrorContainer
            }

            Image(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBackgroundColor)
                    .padding(4.dp),
                imageVector = if(sms.isSent) Icons.Default.Check else Icons.Default.Clear,
                contentDescription = "",
                colorFilter = ColorFilter.tint(iconColor),
            )
        }
    }
}

@Composable
private fun MessageBodyUi(
    sms: SmsUi,
    isExpanded: Boolean = true,
) {
    val animatedBackGroundColor by animateColorAsState(
        if (isExpanded) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.background
        },
        label = "background color",
    )
    val animatedTextColor by animateColorAsState(
        if (isExpanded) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onBackground
        },
        label = "background color",
    )
    Surface(
        modifier = Modifier
            .padding(1.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .animateContentSize(),
        shape = MaterialTheme.shapes.small,
        color = animatedBackGroundColor,
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
            color = animatedTextColor,
            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
private fun SmsListPreviewUi(
    @PreviewParameter(provider = SmsListPreviewParameterProvider::class, limit = 1)
    smsList: SmsList,
) {
    MyTheme {
        SmsListUi(
            smsList = smsList,
        )
    }
}

@Preview
@Composable
private fun MessageBodyExpandedPreviewUi(
    @PreviewParameter(provider = SmsPreviewParameterProvider::class)
    sms: SmsUi,
) {
    MyTheme {
        MessageBodyUi(
            sms = sms,
            isExpanded = true,
        )
    }
}

