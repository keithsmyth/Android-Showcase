package com.keithsmyth.androidshowcase.service

import android.content.Context
import com.keithsmyth.androidshowcase.R
import com.keithsmyth.androidshowcase.service.model.PokemonListServiceModel
import com.keithsmyth.androidshowcase.service.model.Response
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalSerializationApi::class)
@Singleton
class MockPokemonService @Inject constructor(
    @ApplicationContext private val appContext: Context,
) {
    // Moshi implementation: appContext.resources.openRawResource(R.raw.pokemon_list).source().buffer().use { moshiAdapter.fromJson(it) }

    suspend fun list(): Response<PokemonListServiceModel> {
        delay(500)
        return appContext.resources.openRawResource(R.raw.pokemon_list).use { inputStream ->
            Json.decodeFromStream<Response<PokemonListServiceModel>>(inputStream)
        }
    }
}
