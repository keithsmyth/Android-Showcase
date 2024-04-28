package com.keithsmyth.androidshowcase.service.model

import kotlinx.serialization.Serializable

@Serializable
data class NamedApiResource(
    val name: String,
    val url: String,
)
