package com.keithsmyth.androidshowcase.service.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalSerializationApi::class)
class PokemonServiceModelTest {

    private val expected = PokemonServiceModel(
        abilities = listOf(
            PokemonServiceModel.Ability(
                ability = ApiResource("overgrow", "https://pokeapi.co/api/v2/ability/65/"),
                isHidden = false,
                slot = 1,
            ),
            PokemonServiceModel.Ability(
                ability = ApiResource("chlorophyll", "https://pokeapi.co/api/v2/ability/34/"),
                isHidden = true,
                slot = 3,
            ),
        ),
        baseExperience = 64,
        cries = PokemonServiceModel.Cries(
            latest = "https://raw.githubusercontent.com/PokeAPI/cries/main/cries/pokemon/latest/1.ogg",
            legacy = "https://raw.githubusercontent.com/PokeAPI/cries/main/cries/pokemon/legacy/1.ogg",
        ),
        forms = listOf(
            ApiResource("bulbasaur", "https://pokeapi.co/api/v2/pokemon-form/1/"),
        ),
        gameIndices = listOf(
            PokemonServiceModel.GameIndex(
                gameIndex = 153,
                version = ApiResource("red", "https://pokeapi.co/api/v2/version/1/")
            ),
            PokemonServiceModel.GameIndex(
                gameIndex = 153,
                version = ApiResource("blue", "https://pokeapi.co/api/v2/version/2/")
            ),
            PokemonServiceModel.GameIndex(
                gameIndex = 153,
                version = ApiResource("yellow", "https://pokeapi.co/api/v2/version/3/")
            ),
        ),
        height = 7,
        id = 1,
        moves = listOf(
            PokemonServiceModel.Move(
              move = ApiResource("razor-wind", "https://pokeapi.co/api/v2/move/13/"),
                versionGroupDetails = listOf(
                    PokemonServiceModel.MoveVersionGroup(
                        levelLearnedAt = 0,
                        moveLearnMethod = ApiResource("egg", "https://pokeapi.co/api/v2/move-learn-method/2/"),
                        versionGroup = ApiResource("gold-silver", "https://pokeapi.co/api/v2/version-group/3/"),
                    ),
                    PokemonServiceModel.MoveVersionGroup(
                        levelLearnedAt = 0,
                        moveLearnMethod = ApiResource("egg", "https://pokeapi.co/api/v2/move-learn-method/2/"),
                        versionGroup = ApiResource("crystal", "https://pokeapi.co/api/v2/version-group/4/"),
                    ),
                ),
            ),
        ),
        name = "bulbasaur",
        order = 1,
        species = ApiResource("bulbasaur", "https://pokeapi.co/api/v2/pokemon-species/1/"),
        sprites = PokemonServiceModel.Sprites(
            backDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/1.png",
            frontDefault = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png",
            backShiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/shiny/1.png",
            frontShiny = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/1.png",
        ),
        stats = listOf(
            PokemonServiceModel.Stats(
                baseStat = 45,
                effort = 0,
                stat = ApiResource("hp", "https://pokeapi.co/api/v2/stat/1/"),
            ),
            PokemonServiceModel.Stats(
                baseStat = 49,
                effort = 0,
                stat = ApiResource("attack", "https://pokeapi.co/api/v2/stat/2/"),
            ),
        ),
        types = listOf(
            PokemonServiceModel.Types(
                slot = 1,
                type = ApiResource("grass", "https://pokeapi.co/api/v2/type/12/"),
            ),
            PokemonServiceModel.Types(
                slot = 2,
                type = ApiResource("poison", "https://pokeapi.co/api/v2/type/4/"),
            ),
        ),
        weight = 69,
    )

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    @Test
    fun `given trimmed detail json, when decoded, then return correct model`() {
        val model: PokemonServiceModel = loadFile("pokemon_bulbasaur.json")
        assertEquals(expected.abilities, model.abilities)
        assertEquals(expected.baseExperience, model.baseExperience)
        assertEquals(expected.cries, model.cries)
        assertEquals(expected.forms, model.forms)
        assertEquals(expected.gameIndices, model.gameIndices)
        assertEquals(expected.height, model.height)
        assertEquals(expected.id, model.id)
        assertEquals(expected.moves, model.moves)
        assertEquals(expected.name, model.name)
        assertEquals(expected.order, model.order)
        assertEquals(expected.species, model.species)
        assertEquals(expected.sprites, model.sprites)
        assertEquals(expected.stats, model.stats)
        assertEquals(expected.types, model.types)
        assertEquals(expected.weight, model.weight)
    }

    private inline fun <reified T> loadFile(filename: String): T {
        javaClass.classLoader?.getResourceAsStream(filename)?.use { inputStream ->
            return json.decodeFromStream(inputStream)
        } ?: throw RuntimeException("test file read failed for '$filename'")
    }
}
