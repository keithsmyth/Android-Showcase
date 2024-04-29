package com.keithsmyth.androidshowcase.service.model

import com.keithsmyth.androidshowcase.TestResourceLoader.loadFile
import org.junit.Assert.assertEquals
import org.junit.Test

class SpeciesServiceModelTest {

    private val expected = SpeciesServiceModel(
        id = 1,
        name = "bulbasaur",
        order = 1,
        genderRate = 1,
        captureRate = 45,
        baseHappiness = 50,
        hatchCounter = 20,
        eggGroups = listOf(
            NamedApiResource("monster", "https://pokeapi.co/api/v2/egg-group/1/"),
            NamedApiResource("plant", "https://pokeapi.co/api/v2/egg-group/7/"),
        ),
        evolutionChain = ApiResource("https://pokeapi.co/api/v2/evolution-chain/1/"),
        flavorTextEntries = listOf(
            SpeciesServiceModel.FlavorTextEntry(
                flavorText = "A strange seed was\n" +
                        "planted on its\n" +
                        "back at birth.\u000cThe plant sprouts\n" +
                        "and grows with\n" +
                        "this POKéMON.",
                language = NamedApiResource("en", "https://pokeapi.co/api/v2/language/9/"),
                version = NamedApiResource("red", "https://pokeapi.co/api/v2/version/1/"),
            ),
            SpeciesServiceModel.FlavorTextEntry(
                flavorText = "A strange seed was\n" +
                        "planted on its\n" +
                        "back at birth.\u000cThe plant sprouts\n" +
                        "and grows with\n" +
                        "this POKéMON.",
                language = NamedApiResource("en", "https://pokeapi.co/api/v2/language/9/"),
                version = NamedApiResource("blue", "https://pokeapi.co/api/v2/version/2/"),
            ),
        ),
    )

    @Test
    fun `given trimmed species json, when decoded, then return correct model`() {
        val model: SpeciesServiceModel = loadFile("species_bulbasaur.json")
        assertEquals(expected.id, model.id)
        assertEquals(expected.name, model.name)
        assertEquals(expected.order, model.order)
        assertEquals(expected.genderRate, model.genderRate)
        assertEquals(expected.captureRate, model.captureRate)
        assertEquals(expected.baseHappiness, model.baseHappiness)
        assertEquals(expected.hatchCounter, model.hatchCounter)
        assertEquals(expected.eggGroups, model.eggGroups)
        assertEquals(expected.evolutionChain, model.evolutionChain)
        assertEquals(expected.flavorTextEntries, model.flavorTextEntries)
    }
}
