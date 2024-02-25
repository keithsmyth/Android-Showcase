package com.keithsmyth.androidshowcase.view.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.keithsmyth.androidshowcase.view.MainNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val mainNavigation: MainNavigation,
) : ViewModel() {

    init {
        val pokemonId = mainNavigation.pokemonId(savedStateHandle)
        Log.d("DetailViewModel", pokemonId.toString())
    }
}
