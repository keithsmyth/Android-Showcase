package com.keithsmyth.androidshowcase.domain

import com.keithsmyth.androidshowcase.TestDispatcherRule
import com.keithsmyth.androidshowcase.domain.model.ListItemDomainModel
import com.keithsmyth.androidshowcase.service.FakePokemonService
import com.keithsmyth.androidshowcase.service.model.PokemonListServiceModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ListDomainTest {

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    private val serviceModels = listOf(
        PokemonListServiceModel(
            name = "bulbasaur",
            url = "https://pokeapi.co/api/v2/pokemon/1/",
        ),
        PokemonListServiceModel(
            name = "ivysaur",
            url = "https://pokeapi.co/api/v2/pokemon/2/",
        ),
        PokemonListServiceModel(
            name = "venusaur",
            url = "https://pokeapi.co/api/v2/pokemon/3/",
        ),
    )

    private val domainModels = listOf(
        ListItemDomainModel(
            id = 1,
            name = "Bulbasaur",
        ),
        ListItemDomainModel(
            id = 2,
            name = "Ivysaur",
        ),
        ListItemDomainModel(
            id = 3,
            name = "Venusaur",
        ),
    )

    private val pokemonService = FakePokemonService()
    private val dispatchers = testDispatcherRule.mockDispatchers()
    private val subject = ListDomain(pokemonService, dispatchers)

    @Test
    fun `given trailing slash, when mapping domain list, then returns correct id`() =
        runTest {
            // given
            stubPokemonService(listOf(serviceModels.first()))

            // when
            val result = subject.list()

            // then
            assertEquals(domainModels.first().id, result.first().id)
        }

    @Test
    fun `given trailing id, when mapping domain list, then returns correct id`() =
        runTest {
            // given
            stubPokemonService(
                listOf(serviceModels.first().copy(url = "https://pokeapi.co/api/v2/pokemon/1"))
            )

            // when
            val result = subject.list()

            // then
            assertEquals(domainModels.first().id, result.first().id)
        }

    @Test
    fun `given lowercase name, when mapping domain list, then returns capitalized name`() =
        runTest {
            // given
            stubPokemonService(listOf(serviceModels.first()))

            // when
            val result = subject.list()

            // then
            assertEquals(domainModels.first().name, result.first().name)
        }

    @Test
    fun `given multiple items, when mapping domain list, then returns correct ordering`() =
        runTest {
            // given
            stubPokemonService(serviceModels)

            // when
            val result = subject.list()

            // then
            assertEquals(domainModels, result)
        }

    private fun stubPokemonService(results: List<PokemonListServiceModel>) {
        pokemonService.stubList = pokemonService.stubList.copy(
            count = results.size,
            results = results,
        )
    }
}
