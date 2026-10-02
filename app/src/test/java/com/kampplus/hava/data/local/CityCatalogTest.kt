package com.kampplus.hava.data.local

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CityCatalogTest {

    @Test
    fun `getAllCities returns istanbul ankara izmir first, turkish cities until zonguldak, and world cities after zonguldak`() {
        val catalog = CityCatalog(
            turkishCityCatalog = TurkishCityCatalog(),
            worldCityCatalog = WorldCityCatalog(),
        )
        val cities = catalog.getAllCities()

        // 1. Check priority cities
        assertEquals("istanbul", cities[0].id)
        assertEquals("ankara", cities[1].id)
        assertEquals("izmir", cities[2].id)

        // 2. Find Zonguldak index
        val zonguldakIndex = cities.indexOfFirst { it.id == "zonguldak" }
        assertTrue("Zonguldak listede olmalı", zonguldakIndex > 2)

        // 3. Check city after Zonguldak is a world city
        val firstWorldCity = cities[zonguldakIndex + 1]
        assertTrue("Zonguldak'tan sonraki ilk şehir dünya şehri olmalı", firstWorldCity.isWorldCity)
        assertEquals("Amsterdam", firstWorldCity.name)

        // 4. Check last city is Zürih
        val lastCity = cities.last()
        assertEquals("Zürih", lastCity.name)
    }
}
