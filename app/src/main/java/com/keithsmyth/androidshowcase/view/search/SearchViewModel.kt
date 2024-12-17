package com.keithsmyth.androidshowcase.view.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keithsmyth.androidshowcase.common.IdempotentGuard
import com.keithsmyth.androidshowcase.domain.SearchDomain
import com.keithsmyth.androidshowcase.domain.model.ListItemDomainModel
import com.keithsmyth.androidshowcase.domain.model.SearchDomainModel
import com.keithsmyth.androidshowcase.view.MainNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val mainNavigation: MainNavigation,
    private val searchDomain: SearchDomain,
) : ViewModel() {

    private val refreshAction = IdempotentGuard {
        viewModelScope.launch { searchDomain.refresh() }
    }

    data class State(
        val isLoading: Boolean,
        val searchTerm: String,
        val resultBindingItems: List<SearchResultBindingItem>,
        val resultListItems: List<ListItemDomainModel>,
        val allItems: List<SearchResultBindingItem>,
    ) {
        companion object {
            fun default(): State = State(
                isLoading = true,
                searchTerm = "",
                resultBindingItems = emptyList(),
                resultListItems = emptyList(),
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

    fun ensureRefreshList() {
        refreshAction.run()
    }

    fun updateSearchTerm(newSearchTerm: String) {
        searchDomain.updateSearchTerm(newSearchTerm)
    }

    private fun SearchDomainModel.mapToState() = State(
        isLoading = this.isLoading,
        searchTerm = this.searchTerm,
        resultBindingItems = this.results.mapToResultBindingItems(),
        resultListItems = this.results,
        allItems = this.allItems.mapToResultBindingItems(),
    )

    private fun List<ListItemDomainModel>.mapToResultBindingItems() = map { item ->
        SearchResultBindingItem(item, mainNavigation::navigateToDetail)
    }
}
