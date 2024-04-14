package com.keithsmyth.androidshowcase.view.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keithsmyth.androidshowcase.domain.DetailDomain
import com.keithsmyth.androidshowcase.domain.model.PokemonDomainModel
import com.keithsmyth.androidshowcase.view.MainNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mainNavigation: MainNavigation,
    private val detailDomain: DetailDomain,
) : ViewModel() {

    data class State(
        val isLoading: Boolean,
        val detail: PokemonDomainModel?,
    ) {
        companion object {
            fun default(): State = State(
                isLoading = true,
                detail = null,
            )
        }
    }

    private val mutableState = MutableStateFlow(State.default())

    val state: StateFlow<State> = mutableState.asStateFlow()

    init {
        val pokemonId = mainNavigation.pokemonId(savedStateHandle)
        refreshDetail(pokemonId)
    }

    private fun refreshDetail(pokemonId: Int) {
        viewModelScope.launch {
            val pokemon = detailDomain.detail(pokemonId)
            mutableState.emit(
                State(
                    isLoading = false,
                    detail = pokemon,
                )
            )
        }
    }
}
