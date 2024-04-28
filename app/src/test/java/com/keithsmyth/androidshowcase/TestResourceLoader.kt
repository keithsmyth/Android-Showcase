package com.keithsmyth.androidshowcase

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

object TestResourceLoader {

    @OptIn(ExperimentalSerializationApi::class)
    inline fun <reified T> loadFile(filename: String): T {
        val json = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }
        javaClass.classLoader?.getResourceAsStream(filename)?.use { inputStream ->
            return json.decodeFromStream(inputStream)
        } ?: throw RuntimeException("test file read failed for '$filename'")
    }
}
