# Mimari ve Tasarım Rehberi — AirV

**AirV**, katmanlı ve ölçeklenebilir **Clean Architecture** prensiplerine sadık kalınarak, **Feature-First / Layered** hibrit yapısıyla tasarlanmıştır.

## Katmanlar ve Bağımlılık Kuralları

Bağımlılık Yönü: `presentation → domain ← data`

```
  ┌────────────────────────────────────────────────────────┐
  │                   Presentation Katmanı                 │
  │ (Jetpack Compose UI, ViewModels, UI State, Settings)   │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │                      Domain Katmanı                    │
  │     (Saf Kotlin, Domain Modelleri, Repository Arayüzleri)│
  └───────────────────────────▲────────────────────────────┘
                              │
  ┌───────────────────────────┴────────────────────────────┐
  │                       Data Katmanı                     │
  │ (Retrofit API, Room DB, DTO'lar, Catalog, Repositories)│
  └────────────────────────────────────────────────────────┘
```

> **Katman İzolasyon Kuralı:** Domain katmanı saf Kotlin'dir; `android.*`, `androidx.*`, `retrofit2.*` veya `room.*` bağımlılığı barındırmaz. Bu kural [LayerDependencyTest.kt](file:///C:/Projeler/Turkcell%20Bilisim%20Kamp+/app/src/test/java/com/kampplus/hava/architecture/LayerDependencyTest.kt) birim testi ile otomatik denetlenir.

---

## Katman Sorumlulukları

### 1. Presentation Katmanı (`presentation/`)
- **UI State Yönetimi:** `WeatherViewModel`, `StateFlow<LoadState>` ve `StateFlow<UserSettings>` ile ekran durumlarını yayınlar. Ekranlar `collectAsStateWithLifecycle()` kullanarak yaşam döngüsüne duyarlı gözlem yapar.
- **Bileşen Ayrımı (Component Isolation):**
  - [ContentCard.kt](file:///C:/Projeler/Turkcell%20Bilisim%20Kamp+/app/src/main/java/com/kampplus/hava/presentation/components/ContentCard.kt): Hava durumuna özel animasyonlu arka plan ve yüksek kontrastlı tipografi barındıran kart.
  - [AnimatedWeatherBackground.kt](file:///C:/Projeler/Turkcell%20Bilisim%20Kamp+/app/src/main/java/com/kampplus/hava/presentation/components/AnimatedWeatherBackground.kt): `rememberInfiniteTransition()` ve `Canvas` çizimleri ile yağmur, kar, fırtına, yıldız ışıldaması vb. canlı döngüsel efektler.
  - [FavoriteCitiesSection.kt](file:///C:/Projeler/Turkcell%20Bilisim%20Kamp+/app/src/main/java/com/kampplus/hava/presentation/components/FavoriteCitiesSection.kt): Sarı çerçeveli (`#FFFFC107`), eklenme sırasına göre sıralanan favoriler alanı.
  - [SearchBarComponent.kt](file:///C:/Projeler/Turkcell%20Bilisim%20Kamp+/app/src/main/java/com/kampplus/hava/presentation/components/SearchBarComponent.kt): Yuvarlatılmış köşeli arama çubuğu.
  - [SettingsScreen.kt](file:///C:/Projeler/Turkcell%20Bilisim%20Kamp+/app/src/main/java/com/kampplus/hava/presentation/settings/SettingsScreen.kt): Sıcaklık birimi (°C/°F), rüzgar hızı birimi (km/sa / mph) ve tema seçimi ayarları ekranı.
  - [WeatherMetricsCard.kt](file:///C:/Projeler/Turkcell%20Bilisim%20Kamp+/app/src/main/java/com/kampplus/hava/presentation/components/WeatherMetricsCard.kt), [HourlyForecastSection.kt](file:///C:/Projeler/Turkcell%20Bilisim%20Kamp+/app/src/main/java/com/kampplus/hava/presentation/components/HourlyForecastSection.kt), [DailyForecastSection.kt](file:///C:/Projeler/Turkcell%20Bilisim%20Kamp+/app/src/main/java/com/kampplus/hava/presentation/components/DailyForecastSection.kt): Detay ekranı tahmin bileşenleri.

### 2. Domain Katmanı (`domain/`)
- **Modeller:** `CityItem`, `ScreenData`, `CityDetailData`, `HourlyForecast`, `DailyForecast`, `UserSettings`.
- **Sözleşmeler (Interfaces):** `WeatherRepository`, `FavoriteCityRepository`, `SettingsRepository`.

### 3. Data Katmanı (`data/`)
- **Yerel Veri (Local):** 
  - `TurkishCityCatalog`: 81 Türkiye İli.
  - `WorldCityCatalog`: 50 Dünya Şehri.
  - `CityCatalog`: Şehir sıralama mantığı (1. İstanbul, 2. Ankara, 3. İzmir, ardından diğer Türkiye illeri ve Dünya şehirleri Türkçe alfabe sırasında).
  - `HavaDatabase`:
    - `FavoriteCityDao` & `FavoriteCityEntity`: Room veritabanında `favorite_cities` tablosunda `addedAt` zaman damgasıyla tutulan favori şehir verisi.
    - `SettingsDao` & `SettingsEntity`: Room veritabanında `user_settings` tablosunda kalıcı olarak saklanan kullanıcı ayarları (birimler ve tema).
- **Uzak Veri (Remote):**
  - `OpenMeteoApi`: Retrofit 2 arayüzü (`getMultiCityForecast`, `getSingleCityDetailForecast`).
  - `WeatherCodeMapper`: WMO kodlarının cihaz diline göre yerelleştirilmiş Türkçe/İngilizce metinlere dönüştürülmesi.
- **Repository Uygulamaları:**
  - `WeatherRepositoryImpl`: `@IoDispatcher` (`Dispatchers.IO`) üzerinde çalışan, 20'şerli paketlerde (chunks) istek atan ve ana ekran isteklerini hafifleten veri katmanı.
  - `FavoriteCityRepositoryImpl`: Room DAO ile iletişim kuran favori yönetimi.
  - `SettingsRepositoryImpl`: Room DAO ile iletişim kuran ayarlar yönetimi.

---

## Veri Akışı ve Durum Yönetimi (State Flow)

```
Retrofit / Room DB (FavoriteCityDao & SettingsDao)
       │
       ▼
WeatherRepositoryImpl / FavoriteCityRepositoryImpl / SettingsRepositoryImpl (Dispatchers.IO)
       │
       ▼
WeatherViewModel (combine LoadState Flow, FavoriteCityIds & UserSettings)
       │
       ▼
HavaNavHost & MainActivity (collectAsStateWithLifecycle)
       │
       ▼
HomeScreen, DetailScreen & SettingsScreen (Stateless Compose UI)
```

## Durum Sözleşmesi (`LoadState`)

```kotlin
sealed interface LoadState {
    data object Loading : LoadState
    data class Content(val data: ScreenData) : LoadState
    data object Empty : LoadState
    data class Error(val message: String) : LoadState
}
```

Hata yönetimi ve tüm kullanıcı metinleri cihaz diline göre yerelleştirilmiştir:
- İnternet olmaması durumunda: `"No Internet Connection Found"` / `"İnternet Bağlantısı Bulunamadı"`
- Sunucu hatasında: `"Server error or service unavailable"` / `"Sunucu hatası veya servis ulaşılamıyor"`
