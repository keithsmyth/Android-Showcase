package com.keithsmyth.androidshowcase.service.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonServiceModel(
    val abilities: List<Ability>,
    @SerialName("base_experience") val baseExperience: Int,
    val cries: Cries,
    val forms: List<ApiResource>,
    @SerialName("game_indices") val gameIndices: List<GameIndex>,
    val height: Int, // decimetres
    // TODO: held_items val heldItems: List<ApiResource>
    val id: Int,
    val moves: List<Move>,
    val name: String,
    val order: Int,
    val species: ApiResource,
    val sprites: Sprites,
    val stats: List<Stats>,
    val types: List<Types>,
    val weight: Int, // hectograms
) {
    @Serializable
    data class Ability(
        val ability: ApiResource,
        @SerialName("is_hidden") val isHidden: Boolean,
        val slot: Int,
    )

    @Serializable
    data class Cries(
        val latest: String,
        val legacy: String,
    )

    @Serializable
    data class GameIndex(
        @SerialName("game_index") val gameIndex: Int,
        val version: ApiResource,
    )

    @Serializable
    data class Move(
        val move: ApiResource,
        @SerialName("version_group_details") val versionGroupDetails: List<MoveVersionGroup>,
    )

    @Serializable
    data class MoveVersionGroup(
        @SerialName("level_learned_at") val levelLearnedAt: Int,
        @SerialName("move_learn_method") val moveLearnMethod: ApiResource,
        @SerialName("version_group") val versionGroup: ApiResource,
    )

    @Serializable
    data class Sprites(
        @SerialName("back_default") val backDefault: String,
        @SerialName("front_default") val frontDefault: String,
        @SerialName("back_shiny") val backShiny: String,
        @SerialName("front_shiny") val frontShiny: String,
    )

    @Serializable
    data class Stats(
        @SerialName("base_stat") val baseStat: Int,
        val effort: Int,
        val stat: ApiResource,
    )

    @Serializable
    data class Types(
        val slot: Int,
        val type: ApiResource,
    )
}
