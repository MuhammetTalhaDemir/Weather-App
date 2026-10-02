package com.kampplus.hava.data.local

import com.kampplus.hava.domain.model.CityItem
import java.text.Collator
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Türkiye ve Dünya şehirlerinin birleşik kataloğu.
 */
@Singleton
class CityCatalog @Inject constructor(
    private val turkishCityCatalog: TurkishCityCatalog,
    private val worldCityCatalog: WorldCityCatalog,
) {
    private val turkishCollator = Collator.getInstance(Locale.forLanguageTag("tr-TR"))

    private val priorityMap = mapOf(
        "istanbul" to 1,
        "ankara" to 2,
        "izmir" to 3,
    )

    fun getAllCities(): List<CityItem> {
        val turkishCities = turkishCityCatalog.getAllCities().sortedWith { c1, c2 ->
            val p1 = priorityMap[c1.id] ?: Int.MAX_VALUE
            val p2 = priorityMap[c2.id] ?: Int.MAX_VALUE
            if (p1 != p2) {
                p1.compareTo(p2)
            } else {
                turkishCollator.compare(c1.name, c2.name)
            }
        }

        val worldCities = worldCityCatalog.getAllCities().sortedWith { c1, c2 ->
            turkishCollator.compare(c1.name, c2.name)
        }

        return turkishCities + worldCities
    }
}
