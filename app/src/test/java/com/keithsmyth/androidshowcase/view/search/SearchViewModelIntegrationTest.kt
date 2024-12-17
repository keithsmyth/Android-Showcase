package com.keithsmyth.androidshowcase.view.search

import app.cash.turbine.test
import com.keithsmyth.androidshowcase.TestDispatcherRule
import com.keithsmyth.androidshowcase.domain.ListDomain
import com.keithsmyth.androidshowcase.domain.SearchDomain
import com.keithsmyth.androidshowcase.domain.model.ListItemDomainModel
import com.keithsmyth.androidshowcase.service.FakePokemonService
import com.keithsmyth.androidshowcase.service.model.PokemonListServiceModel
import com.keithsmyth.androidshowcase.view.MainNavigation
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class SearchViewModelIntegrationTest {

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    private val mainNavigation: MainNavigation = mock()
    private val fakePokemonService = FakePokemonService()
    private val listDomain = ListDomain(fakePokemonService, testDispatcherRule.mockDispatchers())
    private val searchDomain = SearchDomain(listDomain)
    private lateinit var searchViewModel: SearchViewModel

    @Before
    fun setup() {
        // Must be initialised after Dispatchers.setMain
        searchViewModel = SearchViewModel(mainNavigation, searchDomain)
    }

    @Test
    fun `initial state shows loading`() = runTest {
        searchViewModel.state.test {
            val initialState = awaitItem()
            assertTrue(initialState.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `first load hides loading`() = runTest {
        searchViewModel.state.test {
            awaitItem()

            searchViewModel.ensureRefreshList()

            val firstLoadState = awaitItem()
            assertFalse(firstLoadState.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `first load shows all formatted results`() = runTest {
        val results = listOf(
            PokemonListServiceModel("name 1", "url/1/"),
            PokemonListServiceModel("name 2", "url/2/"),
        )
        val expected = listOf(
            ListItemDomainModel(1, "Name 1"),
            ListItemDomainModel(2, "Name 2"),
        )
        fakePokemonService.stubList = fakePokemonService.stubList.copy(
            count = results.size,
            results = results,
        )

        searchViewModel.state.test {
            awaitItem()

            searchViewModel.ensureRefreshList()

            val firstLoadState = awaitItem()
            assertEquals(expected, firstLoadState.resultListItems)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `second call to refresh is ignored`() = runTest {
        val results = listOf(
            PokemonListServiceModel("name 1", "url/1/"),
            PokemonListServiceModel("name 2", "url/2/"),
        )
        fakePokemonService.stubList = fakePokemonService.stubList.copy(
            count = results.size,
            results = results,
        )

        searchViewModel.state.test {
            awaitItem()

            searchViewModel.ensureRefreshList() // first call
            awaitItem()

            fakePokemonService.stubList = fakePokemonService.stubList.copy(
                count = 0,
                results = emptyList(),
            )
            searchViewModel.ensureRefreshList() // second call
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `update search term filters results`() = runTest {
        val results = listOf(
            PokemonListServiceModel("name 1", "url/1/"),
            PokemonListServiceModel("name 2", "url/2/"),
        )
        val expected = listOf(
            ListItemDomainModel(2, "Name 2"),
        )
        fakePokemonService.stubList = fakePokemonService.stubList.copy(
            count = results.size,
            results = results,
        )

        searchViewModel.state.test {
            awaitItem()

            searchViewModel.ensureRefreshList()
            awaitItem()

            searchViewModel.updateSearchTerm("2")
            val searchResultsState = awaitItem()
            assertEquals(expected, searchResultsState.resultListItems)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
