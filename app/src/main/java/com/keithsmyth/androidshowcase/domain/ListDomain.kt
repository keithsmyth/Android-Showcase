package com.keithsmyth.androidshowcase.domain

import com.keithsmyth.androidshowcase.Dispatchers
import com.keithsmyth.androidshowcase.domain.model.ListItemDomainModel
import com.keithsmyth.androidshowcase.service.MockPokemonService
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ListDomain @Inject constructor(
    private val mockPokemonService: MockPokemonService,
    private val dispatchers: Dispatchers,
) {

    suspend fun list(): List<ListItemDomainModel> {
        return withContext(dispatchers.io()) {
            val response = mockPokemonService.list()
            response.results.map { serviceModel ->
                ListItemDomainModel(
                    id = DomainFormatUtils.idFromApiResourceUrl(serviceModel.url),
                    name = DomainFormatUtils.capitaliseName(serviceModel.name),
                )
            }
        }
    }
}
