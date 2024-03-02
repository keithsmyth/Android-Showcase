package com.keithsmyth.androidshowcase.domain

import com.keithsmyth.androidshowcase.Dispatchers
import com.keithsmyth.androidshowcase.R
import com.keithsmyth.androidshowcase.service.MockPokemonService
import com.keithsmyth.androidshowcase.service.model.ApiResource
import com.keithsmyth.androidshowcase.service.model.PokemonServiceModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyVararg
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class DetailDomainTest {


    private val statHp = PokemonServiceModel.Stats(45, 0, ApiResource("hp", "stat/1/"))
    private val statAttack = PokemonServiceModel.Stats(50, 0, ApiResource("attack", "stat/2/"))
    private val statDefense = PokemonServiceModel.Stats(49, 0, ApiResource("defense", "stat/3/"))
    private val statSpAttack = PokemonServiceModel.Stats(65, 0, ApiResource("sp-atk", "stat/4/"))
    private val statSpDefense = PokemonServiceModel.Stats(66, 0, ApiResource("sp-def", "stat/5/"))
    private val statSpeed = PokemonServiceModel.Stats(46, 0, ApiResource("speed", "stat/6/"))

    private val expectedPrimaryType = ExpectedType(1, 12, "Grass")
    private val expectedSecondaryType = ExpectedType(2, 4, "Poison")
    private val primaryType = expectedPrimaryType.asServiceModel()
    private val secondaryType = expectedSecondaryType.asServiceModel()

    data class ExpectedType(
        val slot: Int,
        val id: Int,
        val name: String,
    )

    private fun ExpectedType.asServiceModel() =
        PokemonServiceModel.Types(slot, ApiResource(name.lowercase(), "type/$id/"))

    private val mockServiceModel = PokemonServiceModel(
        abilities = emptyList(),
        baseExperience = 0,
        cries = PokemonServiceModel.Cries("latest", "legacy"),
        forms = emptyList(),
        gameIndices = emptyList(),
        height = 7,
        id = 1,
        moves = emptyList(),
        name = "test name",
        order = 1,
        species = ApiResource("species", "species/1"),
        sprites = PokemonServiceModel.Sprites("back", "front", "shinyBack", "shinyFront"),
        stats = listOf(
            statHp,
            statAttack,
            statDefense,
            statSpAttack,
            statSpDefense,
            statSpeed,
        ),
        types = listOf(
            primaryType,
            secondaryType,
        ),
        weight = 69,
    )

    private val pokemonService = mock<MockPokemonService> {
        onBlocking { detail(1) } doReturn mockServiceModel
    }

    private val strings = mock<Strings> {
        on { get(any()) } doReturn ""
        on { get(any(), anyVararg()) } doReturn ""
    }

    @Test
    fun `given valid service model, when domain model returned, has same id`() = runTest {
        // given
        val detailDomain = createDetailDomain(testScheduler)

        // when
        val result = detailDomain.detail(1)

        // then
        assertEquals(1, result.id)
    }

    @Test
    fun `given valid service model, when domain model returned, has capitalised name`() = runTest {
        // given
        val detailDomain = createDetailDomain(testScheduler)

        // when
        val result = detailDomain.detail(1)

        // then
        assertEquals("Test name", result.name)
    }

    @Test
    fun `given both cries present, when domain model returned, prefers latest cry`() = runTest {
        // given
        val detailDomain = createDetailDomain(testScheduler)

        // when
        val result = detailDomain.detail(1)

        // then
        assertEquals(mockServiceModel.cries.latest, result.cryUrl)
    }

    @Test
    fun `given only legacy cry present, when domain model returned, uses legacy cry`() = runTest {
        // given
        whenever(pokemonService.detail(1)).doReturn(
            mockServiceModel.copy(
                cries = mockServiceModel.cries.copy(latest = "")
            )
        )
        val detailDomain = createDetailDomain(testScheduler)

        // when
        val result = detailDomain.detail(1)

        // then
        assertEquals(mockServiceModel.cries.legacy, result.cryUrl)
    }

    @Test
    fun `given service height value, when domain height returned, divides by 10 for metres`() =
        runTest {
            // given
            whenever(strings.get(R.string.height_display_value, 0.7)).doReturn("0.7 m")
            val detailDomain = createDetailDomain(testScheduler)

            // when
            val result = detailDomain.detail(1)

            // then
            assertEquals("0.7 m", result.height)
        }

    @Test
    fun `given service weight value, when domain weight returned, divides by 10 for kg`() =
        runTest {
            // given
            whenever(strings.get(R.string.weight_display_value, 6.9)).doReturn("6.9 kg")
            val detailDomain = createDetailDomain(testScheduler)

            // when
            val result = detailDomain.detail(1)

            // then
            assertEquals("6.9 kg", result.weight)
        }

    @Test
    fun `given species ApiResource name, when domain species name, capitalises`() = runTest {
        // given
        val detailDomain = createDetailDomain(testScheduler)

        // when
        val result = detailDomain.detail(1)

        // then
        assertEquals("Species", result.speciesName)
    }

    @Test
    fun `given service model sprites, when domain model mapped, returns all`() = runTest {
        // given
        val detailDomain = createDetailDomain(testScheduler)

        // when
        val result = detailDomain.detail(1)

        // then
        assertEquals(mockServiceModel.sprites.frontDefault, result.spriteFrontUrl)
        assertEquals(mockServiceModel.sprites.backDefault, result.spriteBackUrl)
        assertEquals(mockServiceModel.sprites.frontShiny, result.shinySpriteFrontUrl)
        assertEquals(mockServiceModel.sprites.backShiny, result.shinySpriteBackUrl)
    }

    @Test
    fun `given service model stats, when domain model mapped, returns flattened stats`() = runTest {
        // given
        val detailDomain = createDetailDomain(testScheduler)

        // when
        val result = detailDomain.detail(1)

        // then
        assertEquals(statHp.baseStat, result.statBaseHp)
        assertEquals(statAttack.baseStat, result.statBaseAttack)
        assertEquals(statDefense.baseStat, result.statBaseDefence)
        assertEquals(statSpAttack.baseStat, result.statBaseSpecialAttack)
        assertEquals(statSpDefense.baseStat, result.statBaseSpecialDefence)
        assertEquals(statSpeed.baseStat, result.statBaseSpeed)
    }

    @Test
    fun `given service model stats, when domain model mapped, returns totalled base stats`() =
        runTest {
            // given
            val detailDomain = createDetailDomain(testScheduler)

            // when
            val result = detailDomain.detail(1)

            // then
            assertEquals(
                listOf(
                    statHp,
                    statAttack,
                    statDefense,
                    statSpAttack,
                    statSpDefense,
                    statSpeed,
                ).fold(0) { acc, model ->
                    acc + model.baseStat
                },
                result.totalStat,
            )
        }

    @Test
    fun `given service model type slot 1, when domain model mapped, returns primary type`() =
        runTest {
            // given
            val detailDomain = createDetailDomain(testScheduler)

            // when
            val result = detailDomain.detail(1)

            // then
            assertEquals(expectedPrimaryType.id, result.typePrimaryId)
            assertEquals(expectedPrimaryType.name, result.typePrimaryName)
        }

    @Test
    fun `given service model type slot 2, when domain model mapped, returns secondary type`() =
        runTest {
            // given
            val detailDomain = createDetailDomain(testScheduler)

            // when
            val result = detailDomain.detail(1)

            // then
            assertEquals(expectedSecondaryType.id, result.typeSecondaryId)
            assertEquals(expectedSecondaryType.name, result.typeSecondaryName)
        }

    @Test
    fun `given service model no type slot 2, when domain model mapped, returns no secondary type`() =
        runTest {
            // given
            whenever(pokemonService.detail(1)).doReturn(
                mockServiceModel.copy(
                    types = listOf(primaryType)
                )
            )
            val detailDomain = createDetailDomain(testScheduler)

            // when
            val result = detailDomain.detail(1)

            // then
            assertNull(result.typeSecondaryId)
            assertNull(result.typeSecondaryName)
        }

    @Test
    fun `given service model slots are out of order, when domain model mapped, returns types in order`() =
        runTest {
            // given
            whenever(pokemonService.detail(1)).doReturn(
                mockServiceModel.copy(
                    types = listOf(secondaryType, primaryType)
                )
            )
            val detailDomain = createDetailDomain(testScheduler)

            // when
            val result = detailDomain.detail(1)

            // then
            assertEquals(expectedPrimaryType.id, result.typePrimaryId)
            assertEquals(expectedPrimaryType.name, result.typePrimaryName)
            assertEquals(expectedSecondaryType.id, result.typeSecondaryId)
            assertEquals(expectedSecondaryType.name, result.typeSecondaryName)
        }

    private fun createDetailDomain(testScheduler: TestCoroutineScheduler): DetailDomain {
        val dispatchers = dispatchers(testScheduler)
        return DetailDomain(pokemonService, dispatchers, strings)
    }

    private fun dispatchers(testScheduler: TestCoroutineScheduler): Dispatchers {
        return mock<Dispatchers> {
            on { io() } doReturn StandardTestDispatcher(testScheduler)
        }
    }
}
