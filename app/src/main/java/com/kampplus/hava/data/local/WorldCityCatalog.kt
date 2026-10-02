package com.kampplus.hava.data.local

import com.kampplus.hava.domain.model.CityItem
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Dünyanın önde gelen metropol ve başkentlerinin sabit liste ve koordinat kataloğu.
 */
@Singleton
class WorldCityCatalog @Inject constructor() {
    fun getAllCities(): List<CityItem> = listOf(
        CityItem("amsterdam", "Amsterdam", "", 52.3676, 4.9041, isWorldCity = true),
        CityItem("atina", "Atina", "", 37.9838, 23.7275, isWorldCity = true),
        CityItem("baku", "Bakü", "", 40.4093, 49.8671, isWorldCity = true),
        CityItem("bangkok", "Bangkok", "", 13.7563, 100.5018, isWorldCity = true),
        CityItem("barselona", "Barselona", "", 41.3851, 2.1734, isWorldCity = true),
        CityItem("pekin", "Pekin", "", 39.9042, 116.4074, isWorldCity = true),
        CityItem("berlin", "Berlin", "", 52.5200, 13.4050, isWorldCity = true),
        CityItem("bruksel", "Brüksel", "", 50.8503, 4.3517, isWorldCity = true),
        CityItem("budapeste", "Budapeşte", "", 47.4979, 19.0402, isWorldCity = true),
        CityItem("buenos_aires", "Buenos Aires", "", -34.6037, -58.3816, isWorldCity = true),
        CityItem("kahire", "Kahire", "", 30.0444, 31.2357, isWorldCity = true),
        CityItem("kopenhag", "Kopenhag", "", 55.6761, 12.5683, isWorldCity = true),
        CityItem("dubai", "Dubai", "", 25.2048, 55.2708, isWorldCity = true),
        CityItem("dublin", "Dublin", "", 53.3498, -6.2603, isWorldCity = true),
        CityItem("cenevre", "Cenevre", "", 46.2044, 6.1432, isWorldCity = true),
        CityItem("helsinki", "Helsinki", "", 60.1699, 24.9384, isWorldCity = true),
        CityItem("hong_kong", "Hong Kong", "", 22.3193, 114.1694, isWorldCity = true),
        CityItem("kiev", "Kiev", "", 50.4501, 30.5234, isWorldCity = true),
        CityItem("lizbon", "Lizbon", "", 38.7223, -9.1393, isWorldCity = true),
        CityItem("londra", "Londra", "", 51.5074, -0.1278, isWorldCity = true),
        CityItem("los_angeles", "Los Angeles", "", 34.0522, -118.2437, isWorldCity = true),
        CityItem("madrid", "Madrid", "", 40.4168, -3.7038, isWorldCity = true),
        CityItem("meksiko", "Meksiko", "", 19.4326, -99.1332, isWorldCity = true),
        CityItem("milano", "Milano", "", 45.4642, 9.1900, isWorldCity = true),
        CityItem("montreal", "Montreal", "", 45.5017, -73.5673, isWorldCity = true),
        CityItem("moskova", "Moskova", "", 55.7558, 37.6173, isWorldCity = true),
        CityItem("munih", "Münih", "", 48.1351, 11.5820, isWorldCity = true),
        CityItem("yeni_delhi", "Yeni Delhi", "", 28.6139, 77.2090, isWorldCity = true),
        CityItem("new_york", "New York", "", 40.7128, -74.0060, isWorldCity = true),
        CityItem("oslo", "Oslo", "", 59.9139, 10.7522, isWorldCity = true),
        CityItem("paris", "Paris", "", 48.8566, 2.3522, isWorldCity = true),
        CityItem("prag", "Prag", "", 50.0755, 14.4378, isWorldCity = true),
        CityItem("reykjavik", "Reykjavik", "", 64.1466, -21.9426, isWorldCity = true),
        CityItem("rio_de_janeiro", "Rio de Janeiro", "", -22.9068, -43.1729, isWorldCity = true),
        CityItem("roma", "Roma", "", 41.9028, 12.4964, isWorldCity = true),
        CityItem("san_francisco", "San Francisco", "", 37.7749, -122.4194, isWorldCity = true),
        CityItem("seul", "Seul", "", 37.5665, 126.9780, isWorldCity = true),
        CityItem("sanghay", "Şanghay", "", 31.2304, 121.4737, isWorldCity = true),
        CityItem("singapur", "Singapur", "", 1.3521, 103.8198, isWorldCity = true),
        CityItem("stokholm", "Stokholm", "", 59.3293, 18.0686, isWorldCity = true),
        CityItem("sidney", "Sidney", "", -33.8688, 151.2093, isWorldCity = true),
        CityItem("taskent", "Taşkent", "", 41.2995, 69.2401, isWorldCity = true),
        CityItem("tiflis", "Tiflis", "", 41.7151, 44.8271, isWorldCity = true),
        CityItem("tokyo", "Tokyo", "", 35.6762, 139.6503, isWorldCity = true),
        CityItem("toronto", "Toronto", "", 43.6532, -79.3832, isWorldCity = true),
        CityItem("venedik", "Venedik", "", 45.4408, 12.3155, isWorldCity = true),
        CityItem("viyana", "Viyana", "", 48.2082, 16.3738, isWorldCity = true),
        CityItem("varsova", "Varşova", "", 52.2297, 21.0122, isWorldCity = true),
        CityItem("washington", "Washington", "", 38.9072, -77.0369, isWorldCity = true),
        CityItem("zurih", "Zürih", "", 47.3769, 8.5417, isWorldCity = true),
    )
}
