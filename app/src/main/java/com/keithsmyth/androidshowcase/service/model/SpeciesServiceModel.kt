package com.keithsmyth.androidshowcase.service.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpeciesServiceModel(
    val id: Int,
    val name: String,
    val order: Int,
    @SerialName("gender_rate")
    val genderRate: Int,
    @SerialName("capture_rate")
    val captureRate: Int,
    @SerialName("base_happiness")
    val baseHappiness: Int,
    @SerialName("hatch_counter")
    val hatchCounter: Int,
    @SerialName("egg_groups")
    val eggGroups: List<NamedApiResource>,
    @SerialName("evolution_chain")
    val evolutionChain: ApiResource,
    @SerialName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntry>,
) {
    @Serializable
    data class FlavorTextEntry(
        @SerialName("flavor_text")
        val flavorText: String,
        val language: NamedApiResource,
        val version: NamedApiResource,
    )
}
