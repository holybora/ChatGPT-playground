package com.lvs.data.remote.common

import com.google.gson.annotations.SerializedName

/**
 * Roles for gpt-3.5-turbo model
 */
enum class GPTRole(val value: String) {
    @SerializedName("system")
    SYSTEM("system"),
    @SerializedName("assistant")
    ASSISTANCE("assistant"),
    @SerializedName("user")
    USER("user")
}