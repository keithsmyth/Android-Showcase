package com.keithsmyth.androidshowcase.domain

import com.keithsmyth.androidshowcase.Dispatchers
import com.keithsmyth.androidshowcase.R
import com.keithsmyth.androidshowcase.domain.model.PokemonDomainModel
import com.keithsmyth.androidshowcase.service.MockPokemonService
import com.keithsmyth.androidshowcase.service.model.EvolutionChainServiceModel
import com.keithsmyth.androidshowcase.service.model.PokemonServiceModel
import com.keithsmyth.androidshowcase.service.model.SpeciesServiceModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailDomain @Inject constructor(
    private val mockPokemonService: MockPokemonService,
    private val dispatchers: Dispatchers,
    private val strings: Strings,
) {

    suspend fun detail(pokemonId: Int): PokemonDomainModel {
        return withContext(dispatchers.io()) {
            val pokemonResponse: PokemonServiceModel = mockPokemonService.detail(pokemonId)

            val speciesId = DomainFormatUtils.idFromApiResourceUrl(pokemonResponse.species.url)
            val speciesResponse: SpeciesServiceModel = mockPokemonService.species(speciesId)

            val evolutionId = DomainFormatUtils.idFromApiResourceUrl(speciesResponse.evolutionChain.url)
            val evolutionChainResponse: EvolutionChainServiceModel = mockPokemonService.evolution(evolutionId)

            val statBlock = statBlockFrom(pokemonResponse.stats)

            val primaryTypeBlock = typeBlockFrom(SLOT_PRIMARY, pokemonResponse.types)
                ?: throw IllegalStateException()
            val secondaryTypeBlock = typeBlockFrom(SLOT_SECONDARY, pokemonResponse.types)

            PokemonDomainModel(
                id = pokemonResponse.id,
                name = DomainFormatUtils.capitaliseName(pokemonResponse.name),
                cryUrl = cryUrlFrom(pokemonResponse.cries),
                height = formattedHeight(pokemonResponse.height),
                weight = formattedWeight(pokemonResponse.weight),
                speciesId = speciesId,
                speciesName = DomainFormatUtils.capitaliseName(pokemonResponse.species.name),
                spriteFrontUrl = pokemonResponse.sprites.frontDefault,
                spriteBackUrl = pokemonResponse.sprites.backDefault,
                shinySpriteFrontUrl = pokemonResponse.sprites.frontShiny,
                shinySpriteBackUrl = pokemonResponse.sprites.backShiny,
                statBaseHp = statBlock.statBaseHp,
                statBaseAttack = statBlock.statBaseAttack,
                statBaseDefence = statBlock.statBaseDefence,
                statBaseSpecialAttack = statBlock.statBaseSpecialAttack,
                statBaseSpecialDefence = statBlock.statBaseSpecialDefence,
                statBaseSpeed = statBlock.statBaseSpeed,
                totalStat = statBlock.totalStat,
                typePrimaryId = primaryTypeBlock.id,
                typePrimaryName = primaryTypeBlock.name,
                typeSecondaryId = secondaryTypeBlock?.id,
                typeSecondaryName = secondaryTypeBlock?.name,
            )
        }
    }

    private fun cryUrlFrom(cries: PokemonServiceModel.Cries): String {
        return when {
            cries.latest.isNotBlank() -> cries.latest
            else -> cries.legacy
        }
    }

    private fun formattedHeight(heightDecimetre: Int): String {
        val metreHeight: Double = heightDecimetre.toDouble().div(10)
        return strings.get(R.string.height_display_value, metreHeight)
    }

    private fun formattedWeight(weightHectogram: Int): String {
        val metreWeight: Double = weightHectogram.toDouble().div(10)
        return strings.get(R.string.weight_display_value, metreWeight)
    }

    private fun statBlockFrom(stats: List<PokemonServiceModel.Stats>): StatBlock {
        val idToBaseMap = mutableMapOf<Int, Int>()

        stats.forEach { s ->
            idToBaseMap[DomainFormatUtils.idFromApiResourceUrl(s.stat.url)] = s.baseStat
        }

        fun Map<Int, Int>.require(id: Int): Int = get(id) ?: throw IllegalStateException()

        return StatBlock(
            statBaseHp = idToBaseMap.require(STAT_ID_HP),
            statBaseAttack = idToBaseMap.require(STAT_ID_ATTACK),
            statBaseDefence = idToBaseMap.require(STAT_ID_DEFENSE),
            statBaseSpecialAttack = idToBaseMap.require(STAT_ID_SPECIAL_ATTACK),
            statBaseSpecialDefence = idToBaseMap.require(STAT_ID_SPECIAL_DEFENSE),
            statBaseSpeed = idToBaseMap.require(STAT_ID_SPEED),
            totalStat = listOf(
                idToBaseMap.require(STAT_ID_HP),
                idToBaseMap.require(STAT_ID_ATTACK),
                idToBaseMap.require(STAT_ID_DEFENSE),
                idToBaseMap.require(STAT_ID_SPECIAL_ATTACK),
                idToBaseMap.require(STAT_ID_SPECIAL_DEFENSE),
                idToBaseMap.require(STAT_ID_SPEED),
            ).sum(),
        )
    }

    private fun typeBlockFrom(slot: Int, types: List<PokemonServiceModel.Types>): TypeBlock? {
        return types.firstOrNull { t -> t.slot == slot }?.type?.let { t ->
            TypeBlock(
                id = DomainFormatUtils.idFromApiResourceUrl(t.url),
                name = DomainFormatUtils.capitaliseName(t.name)
            )
        }
    }

    private data class StatBlock(
        val statBaseHp: Int,
        val statBaseAttack: Int,
        val statBaseDefence: Int,
        val statBaseSpecialAttack: Int,
        val statBaseSpecialDefence: Int,
        val statBaseSpeed: Int,
        val totalStat: Int,
    )

    private data class TypeBlock(
        val id: Int,
        val name: String,
    )

    companion object {
        private const val STAT_ID_HP = 1
        private const val STAT_ID_ATTACK = 2
        private const val STAT_ID_DEFENSE = 3
        private const val STAT_ID_SPECIAL_ATTACK = 4
        private const val STAT_ID_SPECIAL_DEFENSE = 5
        private const val STAT_ID_SPEED = 6

        private const val SLOT_PRIMARY = 1
        private const val SLOT_SECONDARY = 2
    }
}
