package com.lvs.data.repositories

import android.util.Log
import com.lvs.data.remote.api.OpenAIApi
import com.lvs.data.remote.api.enities.ChatCompletionRequestBody
import com.lvs.data.remote.api.enities.ChatCompletionResponseBody
import com.lvs.data.remote.api.enities.MessageDto
import com.lvs.data.remote.common.GPTModel
import javax.inject.Inject


class OpenAIRepositoryImpl @Inject constructor(
    private val openAIApi: OpenAIApi,
) : OpenAIRepository {
    companion object {
        private const val TAG = "OpenAIRepositoryImpl"
    }

    override suspend fun textCompletionsWithStream(
        model: GPTModel,
        messages: List<MessageDto>
    ): ChatCompletionResponseBody {

        val response = openAIApi.textCompletions(
            ChatCompletionRequestBody(
                model = model.value,
                messages = messages,
                maxTokens = model.maxTokens / 2

            )
        ).execute()

        return when {
            response.isSuccessful && response.body() != null -> {
                response.body()!!
            }
            else -> {
                Log.e(TAG, "Request failed:  ${response.body()}, please try again")
                throw IllegalStateException("HTTP code:${response.code()}  Request failed")
            }
        }
    }

}