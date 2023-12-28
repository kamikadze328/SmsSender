package com.kamikadze328.smssender.ui.provider

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.kamikadze328.smssender.ui.SmsList
import com.kamikadze328.smssender.ui.SmsUi
import java.util.Date

class SmsPreviewParameterProvider : PreviewParameterProvider<SmsUi> {
    override val values = sequenceOf(
        SmsUi(
            receiverName = "Receiver  awd wad awd aw daw aw daw aw daww ad aw1",
            senderName = "Sender 1",
            text = "Text 1 awd aw daw daw daw aw aw aw aw daw daw daw ",
            isSent = true,
            dateTime = Date().toString(),
        ),
        SmsUi(
            receiverName = "Receiver 2",
            senderName = "Sender 2",
            text = "Text 2 wadjkawhdjkawhjkd kjawhd kjawhd ka kdhaw dh awld hakwjdh kawjh dkjawhd ",
            isSent = false,
            dateTime = Date().toString(),
        ),
    )
}

class SmsListPreviewParameterProvider : PreviewParameterProvider<SmsList> {
    override val values = sequenceOf(
        SmsList(
            list = listOf(
                SmsUi(
                    receiverName = "Receiver 1",
                    senderName = "Sender 1",
                    text = "Text 1",
                    isSent = true,
                    dateTime = Date().toString(),
                ),
                SmsUi(
                    receiverName = "Receiver 2",
                    senderName = "Sender 2",
                    text = "Text 2",
                    isSent = false,
                    dateTime = Date().toString(),
                ),
            )
        ),
    )
}