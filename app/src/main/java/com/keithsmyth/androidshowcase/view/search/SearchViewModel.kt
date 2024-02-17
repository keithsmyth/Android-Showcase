package com.keithsmyth.androidshowcase.view.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keithsmyth.androidshowcase.domain.SearchDomain
import com.keithsmyth.androidshowcase.domain.model.ListItemDomainModel
import com.keithsmyth.androidshowcase.domain.model.SearchDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchDomain: SearchDomain,
) : ViewModel() {

    data class State(
        val isLoading: Boolean,
        val searchTerm: String,
        val results: List<SearchResultBindingItem>,
        val allItems: List<SearchResultBindingItem>,
    ) {
        companion object {
            fun default(): State = State(
                isLoading = true,
                searchTerm = "",
                results = emptyList(),
                allItems = emptyList(),
            )
        }
    }

    val state: StateFlow<State> = searchDomain.model
        .map { model -> model.mapToState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = State.default(),
        )

    init {
        refreshList()
    }

    fun updateSearchTerm(newSearchTerm: String) {
        searchDomain.updateSearchTerm(newSearchTerm)
    }

    private fun refreshList() {
        viewModelScope.launch {
            searchDomain.refresh()
        }
    }

    private fun SearchDomainModel.mapToState() = State(
        isLoading = this.isLoading,
        searchTerm = this.searchTerm,
        results = this.results.mapToResultBindingItems(),
        allItems = this.allItems.mapToResultBindingItems(),
    )

    private fun List<ListItemDomainModel>.mapToResultBindingItems() = map { item ->
        SearchResultBindingItem(item, ::onItemClick)
    }

    private fun onItemClick(id: Int) {}
}
