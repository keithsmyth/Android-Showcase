package com.keithsmyth.androidshowcase.service.model

import kotlinx.serialization.Serializable

@Serializable
data class PokemonListServiceModel(
    val name: String,
    val url: String,
)
