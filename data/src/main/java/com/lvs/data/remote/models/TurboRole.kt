package com.lvs.data.remote.models

import com.google.gson.annotations.SerializedName

enum class TurboRole(val value: String) {
    @SerializedName("system")
    SYSTEM("system"),
    @SerializedName("assistant")
    ASSISTANCE("assistant"),
    @SerializedName("user")
    USER("user")
}