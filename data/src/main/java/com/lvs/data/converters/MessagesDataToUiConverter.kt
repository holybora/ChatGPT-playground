package com.lvs.data.converters

import com.lvs.data.local.main.MessageUiEntity
import com.lvs.data.remote.common.GPTRole
import com.lvs.data.remote.common.toGPTRole
import com.lvs.data.remote.db.entities.MessageEntity

class MessagesDataToUiConverter : BaseConverter<MessageEntity, MessageUiEntity>() {
    override fun convert(from: MessageEntity): MessageUiEntity {
        return MessageUiEntity(
            uid = from.uid,
            conversationId = from.conversationId,
            text = from.text,
            role = from.role.toGPTRole(),
            createdAt = from.createdAt
        )
    }
}