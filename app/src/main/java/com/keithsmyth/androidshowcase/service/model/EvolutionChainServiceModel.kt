package com.keithsmyth.androidshowcase.service.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EvolutionChainServiceModel(
    val id: Int,
    val chain: Chain,
) {
    @Serializable
    data class Chain(
        val species: NamedApiResource,
        @SerialName("evolves_to") val evolvesTo: List<Chain>,
        @SerialName("evolution_details") val evolutionDetails: List<EvolutionDetail>,
    )

    @Serializable
    data class EvolutionDetail(
        @SerialName("min_level") val minLevel: Int?,
        val trigger: NamedApiResource,
    )
}
