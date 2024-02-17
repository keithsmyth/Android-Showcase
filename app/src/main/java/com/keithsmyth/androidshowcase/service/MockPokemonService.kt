package com.keithsmyth.androidshowcase.service

import android.content.Context
import com.keithsmyth.androidshowcase.R
import com.keithsmyth.androidshowcase.service.model.ApiResponse
import com.keithsmyth.androidshowcase.service.model.PokemonListServiceModel
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
    private val json: Json,
) {
    // Moshi implementation: appContext.resources.openRawResource(R.raw.pokemon_list).source().buffer().use { moshiAdapter.fromJson(it) }

    suspend fun list(): ApiResponse<PokemonListServiceModel> {
        delay(500)
        return appContext.resources.openRawResource(R.raw.pokemon_list).use { inputStream ->
            json.decodeFromStream<ApiResponse<PokemonListServiceModel>>(inputStream)
        }
    }
}
