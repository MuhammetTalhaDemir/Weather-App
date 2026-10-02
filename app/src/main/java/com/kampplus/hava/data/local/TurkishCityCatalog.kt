package com.kampplus.hava.data.local

import com.kampplus.hava.domain.model.CityItem
import java.text.Collator
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Türkiye'nin 81 ilinin sabit liste ve koordinat kataloğu.
 */
@Singleton
class TurkishCityCatalog @Inject constructor() {

    private val turkishCollator = Collator.getInstance(Locale.forLanguageTag("tr-TR"))

    private val priorityMap = mapOf(
        "istanbul" to 1,
        "ankara" to 2,
        "izmir" to 3,
    )

    private val rawCities = listOf(
        CityItem("adana", "Adana", "", 37.0, 35.3213),
        CityItem("adiyaman", "Adıyaman", "", 37.7644, 38.2786),
        CityItem("afyonkarahisar", "Afyonkarahisar", "", 38.7507, 30.5567),
        CityItem("agri", "Ağrı", "", 39.7191, 43.0514),
        CityItem("amasya", "Amasya", "", 40.6499, 35.8353),
        CityItem("ankara", "Ankara", "", 39.9334, 32.8597),
        CityItem("antalya", "Antalya", "", 36.8841, 30.7056),
        CityItem("artvin", "Artvin", "", 41.1828, 41.8183),
        CityItem("aydin", "Aydın", "", 37.856, 27.8416),
        CityItem("balikesir", "Balıkesir", "", 39.6484, 27.8826),
        CityItem("bilecik", "Bilecik", "", 40.1501, 29.9792),
        CityItem("bingol", "Bingöl", "", 38.8851, 40.498),
        CityItem("bitlis", "Bitlis", "", 38.4006, 42.1095),
        CityItem("bolu", "Bolu", "", 40.7358, 31.6061),
        CityItem("burdur", "Burdur", "", 37.7203, 30.2908),
        CityItem("bursa", "Bursa", "", 40.1885, 29.061),
        CityItem("canakkale", "Çanakkale", "", 40.1553, 26.4142),
        CityItem("cankiri", "Çankırı", "", 40.6013, 33.6134),
        CityItem("corum", "Çorum", "", 40.5506, 34.9556),
        CityItem("denizli", "Denizli", "", 37.7765, 29.0864),
        CityItem("diyarbakir", "Diyarbakır", "", 37.9144, 40.2306),
        CityItem("edirne", "Edirne", "", 41.6772, 26.5557),
        CityItem("elazig", "Elazığ", "", 38.681, 39.2264),
        CityItem("erzincan", "Erzincan", "", 39.75, 39.5),
        CityItem("erzurum", "Erzurum", "", 39.9043, 41.2679),
        CityItem("eskisehir", "Eskişehir", "", 39.7767, 30.5206),
        CityItem("gaziantep", "Gaziantep", "", 37.0662, 37.3833),
        CityItem("giresun", "Giresun", "", 40.9128, 38.3895),
        CityItem("gumushane", "Gümüşhane", "", 40.4602, 39.4814),
        CityItem("hakkari", "Hakkari", "", 37.5833, 43.7333),
        CityItem("hatay", "Hatay", "", 36.2023, 36.1613),
        CityItem("isparta", "Isparta", "", 37.7648, 30.5566),
        CityItem("mersin", "Mersin", "", 36.8, 34.6333),
        CityItem("istanbul", "İstanbul", "", 41.0082, 28.9784),
        CityItem("izmir", "İzmir", "", 38.4237, 27.1428),
        CityItem("kars", "Kars", "", 40.6172, 43.0953),
        CityItem("kastamonu", "Kastamonu", "", 41.3887, 33.7827),
        CityItem("kayseri", "Kayseri", "", 38.7312, 35.4787),
        CityItem("kirklareli", "Kırklareli", "", 41.7333, 27.2167),
        CityItem("kirsehir", "Kırşehir", "", 39.1425, 34.1709),
        CityItem("kocaeli", "Kocaeli", "", 40.8533, 29.8815),
        CityItem("konya", "Konya", "", 37.8714, 32.4846),
        CityItem("kutahya", "Kütahya", "", 39.4167, 29.9833),
        CityItem("malatya", "Malatya", "", 38.3552, 38.3095),
        CityItem("manisa", "Manisa", "", 38.6191, 27.4289),
        CityItem("kahramanmaras", "Kahramanmaraş", "", 37.5858, 36.9371),
        CityItem("mardin", "Mardin", "", 37.3212, 40.7245),
        CityItem("mugla", "Muğla", "", 37.2153, 28.3636),
        CityItem("mus", "Muş", "", 38.7432, 41.5064),
        CityItem("nevsehir", "Nevşehir", "", 38.6244, 34.7144),
        CityItem("nigde", "Niğde", "", 37.9667, 34.6833),
        CityItem("ordu", "Ordu", "", 40.9839, 37.8764),
        CityItem("rize", "Rize", "", 41.0201, 40.5234),
        CityItem("sakarya", "Sakarya", "", 40.7569, 30.3783),
        CityItem("samsun", "Samsun", "", 41.2928, 36.3313),
        CityItem("siirt", "Siirt", "", 37.9326, 41.9403),
        CityItem("sinop", "Sinop", "", 42.0231, 35.1531),
        CityItem("sivas", "Sivas", "", 39.7477, 37.0179),
        CityItem("tekirdag", "Tekirdağ", "", 40.9833, 27.5167),
        CityItem("tokat", "Tokat", "", 40.3167, 36.55),
        CityItem("trabzon", "Trabzon", "", 41.0015, 39.7178),
        CityItem("tunceli", "Tunceli", "", 39.1079, 39.5401),
        CityItem("sanliurfa", "Şanlıurfa", "", 37.1674, 38.7954),
        CityItem("usak", "Uşak", "", 38.6823, 29.4082),
        CityItem("van", "Van", "", 38.4891, 43.4089),
        CityItem("yozgat", "Yozgat", "", 39.8181, 34.8147),
        CityItem("zonguldak", "Zonguldak", "", 41.4564, 31.7987),
        CityItem("aksaray", "Aksaray", "", 38.3687, 34.037),
        CityItem("bayburt", "Bayburt", "", 40.2552, 40.2249),
        CityItem("karaman", "Karaman", "", 37.1759, 33.2287),
        CityItem("kirikkale", "Kırıkkale", "", 39.8453, 33.5064),
        CityItem("batman", "Batman", "", 37.8812, 41.1351),
        CityItem("sirnak", "Şırnak", "", 37.5164, 42.4611),
        CityItem("bartin", "Bartın", "", 41.6358, 32.3375),
        CityItem("ardahan", "Ardahan", "", 41.1105, 42.7022),
        CityItem("igdir", "Iğdır", "", 39.9167, 44.0333),
        CityItem("yalova", "Yalova", "", 40.655, 29.2769),
        CityItem("karabuk", "Karabük", "", 41.2061, 32.6204),
        CityItem("kilis", "Kilis", "", 36.7184, 37.1212),
        CityItem("osmaniye", "Osmaniye", "", 37.0742, 36.2478),
        CityItem("duzce", "Düzce", "", 40.8438, 31.1565),
    )

    fun getAllCities(): List<CityItem> {
        return rawCities.sortedWith { c1, c2 ->
            val p1 = priorityMap[c1.id] ?: Int.MAX_VALUE
            val p2 = priorityMap[c2.id] ?: Int.MAX_VALUE
            if (p1 != p2) {
                p1.compareTo(p2)
            } else {
                turkishCollator.compare(c1.name, c2.name)
            }
        }
    }
}
