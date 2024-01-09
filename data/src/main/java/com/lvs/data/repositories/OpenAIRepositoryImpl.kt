package com.lvs.data.repositories

import android.util.Log
import com.lvs.data.remote.api.OpenAIApi
import com.lvs.data.remote.api.enities.ChatCompletionRequestBody
import com.lvs.data.remote.api.enities.ChatCompletionResponseBody
import com.lvs.data.remote.api.enities.MessageDto
import com.lvs.data.remote.api.enities.SpeechToTextResponseBody
import com.lvs.data.remote.common.GPTModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
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

    override suspend fun speechToText(filePath: String): SpeechToTextResponseBody {

        val file = File(filePath)
        val requestFile = file.asRequestBody(contentType = "audio/mpeg".toMediaType())
        val response = openAIApi.speechToTextUpload(
            model = "whisper-1".toRequestBody("text/plain".toMediaType()),
            file = MultipartBody.Part.createFormData("file", file.name, requestFile)

        )
            .execute()

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