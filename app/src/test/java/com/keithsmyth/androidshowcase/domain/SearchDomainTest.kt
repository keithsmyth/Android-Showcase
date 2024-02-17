package com.keithsmyth.androidshowcase.domain

import com.keithsmyth.androidshowcase.domain.model.ListItemDomainModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class SearchDomainTest {

    private val listModels = listOf(
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
    fun `given search term with case insensitive matches, when search term updated, then returns all matches`() =
        runTest {
            // given
            val listDomain = mockListDomain(listModels)
            val searchDomain = SearchDomain(listDomain)
            searchDomain.refresh()

            // when
            searchDomain.updateSearchTerm("v")
            val result = searchDomain.model.value

            // then
            assertEquals(listModels.filter { it.id == 2 || it.id == 3 }, result.results)
            assertEquals("v", result.searchTerm)
            assertEquals(listModels, result.allItems)
        }

    @Test
    fun `given search term with matches but no list refreshed, when search term updated, then returns no matches`() =
        runTest {
            // given
            val listDomain = mockListDomain(emptyList())
            val searchDomain = SearchDomain(listDomain)

            // when
            searchDomain.updateSearchTerm("v")
            val result = searchDomain.model.value

            // then
            assertEquals(emptyList<ListItemDomainModel>(), result.results)
            assertEquals("v", result.searchTerm)
            assertEquals(emptyList<ListItemDomainModel>(), result.allItems)
        }

    @Test
    fun `given empty search term, when list refreshed, then returns entire list`() = runTest {
        // given
        val listDomain = mockListDomain(listModels)
        val searchDomain = SearchDomain(listDomain)
        searchDomain.refresh()

        // when
        val result = searchDomain.model.value

        // then
        assertEquals(listModels, result.results)
        assertEquals("", result.searchTerm)
        assertEquals(listModels, result.allItems)
    }

    @Test
    fun `given blank search term, when search term updated, then returns entire list`() = runTest {
        // given
        val listDomain = mockListDomain(listModels)
        val searchDomain = SearchDomain(listDomain)
        searchDomain.refresh()

        // when
        searchDomain.updateSearchTerm("  ")
        val result = searchDomain.model.value

        // then
        assertEquals(listModels, result.results)
        assertEquals("  ", result.searchTerm)
        assertEquals(listModels, result.allItems)
    }

    @Test
    fun `given search term with no matches, when search term updated, then returns empty list`() =
        runTest {
            // given
            val listDomain = mockListDomain(listModels)
            val searchDomain = SearchDomain(listDomain)
            searchDomain.refresh()

            // when
            searchDomain.updateSearchTerm("no match")
            val result = searchDomain.model.value

            // then
            assertEquals(emptyList<ListItemDomainModel>(), result.results)
            assertEquals("no match", result.searchTerm)
            assertEquals(listModels, result.allItems)
        }

    @Test
    fun `given items not loaded, when search model observed, then returns loading state`() =
        runTest {
            // given
            val listDomain = mockListDomain(listModels)
            val searchDomain = SearchDomain(listDomain)

            // when
            val result = searchDomain.model.value

            // then
            assertTrue(result.isLoading)
        }

    @Test
    fun `given items are loaded, when search model observed, then returns loaded state`() =
        runTest {
            // given
            val listDomain = mockListDomain(emptyList())
            val searchDomain = SearchDomain(listDomain)

            // when
            searchDomain.refresh()
            val result = searchDomain.model.value

            // then
            assertFalse(result.isLoading)
        }

    private fun mockListDomain(list: List<ListItemDomainModel>): ListDomain {
        return mock<ListDomain> {
            onBlocking { list() } doReturn list
        }
    }
}
