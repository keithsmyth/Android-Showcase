package com.keithsmyth.androidshowcase.domain

import com.keithsmyth.androidshowcase.Dispatchers
import com.keithsmyth.androidshowcase.domain.model.ListItemDomainModel
import com.keithsmyth.androidshowcase.service.PokemonService
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListDomain @Inject constructor(
    private val pokemonService: PokemonService,
    private val dispatchers: Dispatchers,
) {

    suspend fun list(): List<ListItemDomainModel> {
        return withContext(dispatchers.io()) {
            val response = pokemonService.list()
            response.results.map { serviceModel ->
                ListItemDomainModel(
                    id = DomainFormatUtils.idFromApiResourceUrl(serviceModel.url),
                    name = DomainFormatUtils.capitaliseName(serviceModel.name),
                )
            }
        }
    }
}
