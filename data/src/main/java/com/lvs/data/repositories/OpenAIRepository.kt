package com.lvs.data.repositories

import com.lvs.data.remote.api.enities.ChatCompletionResponseBody
import com.lvs.data.remote.api.enities.MessageDto
import com.lvs.data.remote.common.GPTModel

interface OpenAIRepository {
    suspend fun textCompletionsWithStream(model: GPTModel, messages: List<MessageDto>): ChatCompletionResponseBody
}