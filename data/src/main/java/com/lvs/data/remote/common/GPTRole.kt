package com.lvs.data.remote.common

import com.google.gson.annotations.SerializedName
import java.lang.IllegalStateException

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

fun String.toGPTRole(): GPTRole = when (this) {
    GPTRole.SYSTEM.value -> GPTRole.SYSTEM
    GPTRole.ASSISTANCE.value -> GPTRole.ASSISTANCE
    GPTRole.USER.value -> GPTRole.USER
    else -> throw IllegalArgumentException(" No enum constant for $this")
}