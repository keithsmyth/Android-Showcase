package com.keithsmyth.androidshowcase.domain

import com.keithsmyth.androidshowcase.domain.model.ListItemDomainModel
import com.keithsmyth.androidshowcase.domain.model.SearchDomainModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SearchDomain @Inject constructor(
    private val listDomain: ListDomain,
) {
    private val mutableModel = MutableStateFlow(SearchDomainModel.default())

    val model: StateFlow<SearchDomainModel> = mutableModel.asStateFlow()

    suspend fun refresh() {
        val list = listDomain.list()
        updateSearchItems(list)
    }

    fun updateSearchTerm(newTerm: String) {
        mutableModel.update { current ->
            current.copy(
                searchTerm = newTerm,
                results = current.allItems.filterBy(newTerm),
            )
        }
    }

    private fun updateSearchItems(list: List<ListItemDomainModel>) {
        mutableModel.update { current ->
            current.copy(
                isLoading = false,
                allItems = list,
                results = list.filterBy(current.searchTerm)
            )
        }
    }

    private fun List<ListItemDomainModel>.filterBy(searchTerm: String): List<ListItemDomainModel> {
        return if (searchTerm.isBlank()) this
        else filter { item -> item.name.contains(searchTerm, ignoreCase = true) }
    }
}
