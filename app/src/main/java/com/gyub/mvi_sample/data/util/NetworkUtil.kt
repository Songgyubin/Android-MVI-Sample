package com.gyub.mvi_sample.data.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

fun getPrettyLog(text: String?): String {
    text ?: return ""

    return try {
        val json = Json { prettyPrint = true }
        val jsonElement = json.decodeFromString<JsonObject>(text)
        json.encodeToString(JsonObject.serializer(), jsonElement)
    } catch (e: Exception) {
        text
    }
}