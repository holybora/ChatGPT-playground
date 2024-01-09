package com.lvs.data.remote.api

import com.lvs.data.remote.api.enities.ChatCompletionRequestBody
import com.lvs.data.remote.api.enities.ChatCompletionResponseBody
import com.lvs.data.remote.api.enities.SpeechToTextResponseBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface OpenAIApi {
    @POST("v1/chat/completions")
    @Headers("Accept: application/json")
    fun textCompletions(@Body body: ChatCompletionRequestBody): Call<ChatCompletionResponseBody>

    @Multipart
    @POST("v1/audio/transcriptions")
    fun speechToTextUpload(
        @Part("model") model: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<SpeechToTextResponseBody?>


}