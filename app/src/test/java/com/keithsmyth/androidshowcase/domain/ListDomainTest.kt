package com.keithsmyth.androidshowcase.domain

import com.keithsmyth.androidshowcase.Dispatchers
import com.keithsmyth.androidshowcase.domain.model.ListItemDomainModel
import com.keithsmyth.androidshowcase.service.MockPokemonService
import com.keithsmyth.androidshowcase.service.model.PokemonListServiceModel
import com.keithsmyth.androidshowcase.service.model.ApiResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class ListDomainTest {

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

    @Test
    fun `given trailing slash, when mapping domain list, then returns correct id`() =
        runTest {
            // given
            val pokemonService = mockPokemonService(listOf(serviceModels.first()))
            val dispatchers = dispatchers(testScheduler)
            val listDomain = ListDomain(pokemonService, dispatchers)

            // when
            val result = listDomain.list()

            // then
            assertEquals(domainModels.first().id, result.first().id)
        }

    @Test
    fun `given trailing id, when mapping domain list, then returns correct id`() =
        runTest {
            // given
            val pokemonService = mockPokemonService(
                listOf(
                    serviceModels.first().copy(url = "https://pokeapi.co/api/v2/pokemon/1"),
                )
            )
            val dispatchers = dispatchers(testScheduler)
            val listDomain = ListDomain(pokemonService, dispatchers)

            // when
            val result = listDomain.list()

            // then
            assertEquals(domainModels.first().id, result.first().id)
        }

    @Test
    fun `given lowercase name, when mapping domain list, then returns capitalized name`() =
        runTest {
            // given
            val pokemonService = mockPokemonService(listOf(serviceModels.first()))
            val dispatchers = dispatchers(testScheduler)
            val listDomain = ListDomain(pokemonService, dispatchers)

            // when
            val result = listDomain.list()

            // then
            assertEquals(domainModels.first().name, result.first().name)
        }

    @Test
    fun `given multiple items, when mapping domain list, then returns correct ordering`() =
        runTest {
            // given
            val pokemonService = mockPokemonService(serviceModels)
            val dispatchers = dispatchers(testScheduler)
            val listDomain = ListDomain(pokemonService, dispatchers)

            // when
            val result = listDomain.list()

            // then
            assertEquals(domainModels, result)
        }

    private fun mockPokemonService(results: List<PokemonListServiceModel>): MockPokemonService {
        return mock<MockPokemonService> {
            onBlocking { list() } doReturn ApiResponse(
                count = results.size,
                next = null,
                previous = null,
                results = results,
            )
        }
    }

    private fun dispatchers(testScheduler: TestCoroutineScheduler): Dispatchers {
        return mock<Dispatchers> {
            on { io() } doReturn StandardTestDispatcher(testScheduler)
        }
    }
}
