package com.keithsmyth.androidshowcase.service

import com.keithsmyth.androidshowcase.service.model.ApiResource
import com.keithsmyth.androidshowcase.service.model.ApiResponse
import com.keithsmyth.androidshowcase.service.model.EvolutionChainServiceModel
import com.keithsmyth.androidshowcase.service.model.NamedApiResource
import com.keithsmyth.androidshowcase.service.model.PokemonListServiceModel
import com.keithsmyth.androidshowcase.service.model.PokemonServiceModel
import com.keithsmyth.androidshowcase.service.model.SpeciesServiceModel

class FakePokemonService : PokemonService {

    var stubList: ApiResponse<PokemonListServiceModel> = ApiResponse(
        count = 0,
        next = null,
        previous = null,
        results = emptyList(),
    )

    var stubDetail = PokemonServiceModel(
        abilities = emptyList(),
        baseExperience = 0,
        cries = PokemonServiceModel.Cries("", ""),
        forms = emptyList(),
        gameIndices = emptyList(),
        height = 0,
        id = 0,
        moves = emptyList(),
        name = "",
        order = 0,
        species = NamedApiResource("", ""),
        sprites = PokemonServiceModel.Sprites("", "", "", ""),
        stats = emptyList(),
        types = emptyList(),
        weight = 0,
    )

    var stubSpecies = SpeciesServiceModel(
        id = 0,
        name = "",
        order = 0,
        genderRate = 0,
        captureRate = 0,
        baseHappiness = 0,
        hatchCounter = 0,
        eggGroups = emptyList(),
        evolutionChain = ApiResource(""),
        flavorTextEntries = emptyList(),
    )

    var stubEvolution = EvolutionChainServiceModel(
        id = 0,
        chain = EvolutionChainServiceModel.Chain(
            species = NamedApiResource("", ""),
            evolvesTo = emptyList(),
            evolutionDetails = emptyList(),
        ),
    )

    override suspend fun list(): ApiResponse<PokemonListServiceModel> {
        return stubList.copy(results = stubList.results.toList())
    }

    override suspend fun detail(id: Int): PokemonServiceModel = stubDetail.copy()

    override suspend fun species(speciesId: Int): SpeciesServiceModel = stubSpecies.copy()

    override suspend fun evolution(evolutionId: Int): EvolutionChainServiceModel =
        stubEvolution.copy()
}
