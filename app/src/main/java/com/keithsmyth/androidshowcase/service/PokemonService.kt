package com.keithsmyth.androidshowcase.service

import com.keithsmyth.androidshowcase.service.model.ApiResponse
import com.keithsmyth.androidshowcase.service.model.EvolutionChainServiceModel
import com.keithsmyth.androidshowcase.service.model.PokemonListServiceModel
import com.keithsmyth.androidshowcase.service.model.PokemonServiceModel
import com.keithsmyth.androidshowcase.service.model.SpeciesServiceModel

interface PokemonService {
    suspend fun list(): ApiResponse<PokemonListServiceModel>
    suspend fun detail(id: Int): PokemonServiceModel
    suspend fun species(speciesId: Int): SpeciesServiceModel
    suspend fun evolution(evolutionId: Int): EvolutionChainServiceModel
}
