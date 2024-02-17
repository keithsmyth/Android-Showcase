package com.keithsmyth.androidshowcase.service.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>,
)
