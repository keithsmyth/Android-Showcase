package com.keithsmyth.androidshowcase.domain.model

data class SearchDomainModel(
    val isLoading: Boolean,
    val searchTerm: String,
    val results: List<ListItemDomainModel>,
    val allItems: List<ListItemDomainModel>,
) {
    companion object {
        fun default(): SearchDomainModel = SearchDomainModel(
            isLoading = true,
            searchTerm = "",
            results = emptyList(),
            allItems = emptyList(),
        )
    }
}
