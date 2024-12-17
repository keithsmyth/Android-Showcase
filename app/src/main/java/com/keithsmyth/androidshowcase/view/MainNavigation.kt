package com.keithsmyth.androidshowcase.view

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.createGraph
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.fragment
import com.keithsmyth.androidshowcase.ReleaseToggles
import com.keithsmyth.androidshowcase.view.detail.DetailComposeFragment
import com.keithsmyth.androidshowcase.view.detail.DetailFragment
import com.keithsmyth.androidshowcase.view.search.SearchComposeFragment
import com.keithsmyth.androidshowcase.view.search.SearchFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainNavigation @Inject constructor() {

    fun initNavController(navController: NavController) {
        navController.graph = navController.createGraph(
            startDestination = DEST_SEARCH
        ) {

            if (ReleaseToggles.COMPOSE_SEARCH) {
                fragment<SearchComposeFragment>(DEST_SEARCH)
            } else {
                fragment<SearchFragment>(DEST_SEARCH)
            }

            val detailRoute = "$DEST_DETAIL/{$ARG_POKEMON_ID}"
            val detailArgBuilder: FragmentNavigatorDestinationBuilder.() -> Unit = {
                argument(ARG_POKEMON_ID) {
                    type = NavType.IntType
                }
            }

            if (ReleaseToggles.COMPOSE_DETAIL) {
                fragment<DetailComposeFragment>(detailRoute) {
                    detailArgBuilder()
                }
            } else {
                fragment<DetailFragment>(detailRoute) {
                    detailArgBuilder()
                }
            }
        }
    }

    fun navigateToDetail(pokemonId: Int, navController: NavController) {
        navController.navigate("$DEST_DETAIL/$pokemonId")
    }

    fun pokemonId(savedStateHandle: SavedStateHandle): Int {
        return savedStateHandle.get<Int>(ARG_POKEMON_ID) ?: throw IllegalArgumentException()
    }

    companion object {
        private const val DEST_SEARCH = "search"
        private const val DEST_DETAIL = "detail"
        private const val ARG_POKEMON_ID = "pokemon_id"
    }
}
