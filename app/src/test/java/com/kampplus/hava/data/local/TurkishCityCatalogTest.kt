package com.kampplus.hava.data.local

import org.junit.Assert.assertEquals
import org.junit.Test

class TurkishCityCatalogTest {

    @Test
    fun `getAllCities returns istanbul ankara izmir first and rest in turkish alphabetical order`() {
        val catalog = TurkishCityCatalog()
        val cities = catalog.getAllCities()

        assertEquals("istanbul", cities[0].id)
        assertEquals("ankara", cities[1].id)
        assertEquals("izmir", cities[2].id)

        val rest = cities.asSequence().drop(3).map { it.name }.toList()
        assertEquals("Adana", rest[0])
        assertEquals("Adıyaman", rest[1])
        assertEquals("Afyonkarahisar", rest[2])
        assertEquals("Ağrı", rest[3])
    }
}
