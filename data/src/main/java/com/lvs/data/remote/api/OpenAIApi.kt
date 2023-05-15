package com.lvs.data.remote.api

import com.lvs.data.remote.api.enities.ChatCompletionRequestBody
import com.lvs.data.remote.api.enities.ChatCompletionResponseBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

const val textCompletionsTurboEndpoint = "v1/chat/completions"

interface OpenAIApi {
    @POST(textCompletionsTurboEndpoint)
    @Headers("Accept: application/json")
    fun textCompletions(@Body body: ChatCompletionRequestBody): Call<ChatCompletionResponseBody>


}