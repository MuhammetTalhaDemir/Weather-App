# AirV — Hava Durumu Keşif Uygulaması

**AirV**, Android platformu için geliştirilmiş, modern **Jetpack Compose**, **Clean Architecture** ve **MVVM** tasarım kalıplarını kullanan gelişmiş bir hava durumu keşif ve takip uygulamasıdır. 

Türkiye'nin 81 ilinin ve dünyanın 50 önde gelen metropolünün anlık hava durumunu listeler, seçilen şehrin 24 saatlik saatlik ve 7 günlük detaylı tahminlerini sunar, favori şehirleri ve kullanıcı ayarlarını cihazın yerel Room veritabanında saklar.

**Teknoloji Yığını:** Kotlin · Jetpack Compose (Material 3) · Clean Architecture · Coroutines & Flow (`StateFlow`) · Dagger Hilt · Retrofit 2 & OkHttp 4 · Room Database · Navigation Compose (Type-Safe)

---

## 🌟 Öne Çıkan Özellikler

- 🌐 **Çoklu Dil Desteği (English & Türkçe Localization):**
  - Cihaz dili Türkçe olan kullanıcılar için tam Türkçe arayüz (`values-tr/strings.xml`).
  - Cihaz dili İngilizce veya farklı olan kullanıcılar için tam İngilizce arayüz (`values/strings.xml`).
- ⚙️ **Uygulama İçi Ayarlar & Birim Dönüşümleri (Settings Screen):**
  - **Sıcaklık Birimi:** Celsius (°C) / Fahrenheit (°F) seçimi.
  - **Rüzgâr Hızı Birimi:** Kilometre/Saat (km/sa) / Mil/Saat (mph) seçimi.
  - **Uygulama Teması:** Sistem Varsayılanı / Aydınlık Tema / Karanlık Tema seçimi.
  - Tercihler Room Veritabanında kalıcı olarak saklanır; tüm ekranlardaki sıcaklık ve rüzgar değerleri anında reaktif olarak dönüştürülür.
- ☀️ **Canlı & Animasyonlu Hava Durumu Görselleri:**
  - Güneşli, Az Bulutlu, Çok Bulutlu, Sisli, Yağmurlu, Karlı, Dolu ve Gökgürültülü Fırtına için özel **Android Vector Drawable** arka plan tasarımları.
  - Compose `Canvas` ve `rememberInfiniteTransition` ile oluşturulmuş, yüksek performanslı sonsuz döngülü (loop) canlı animasyonlar (süzülen yağmur damlaları, dökülen kar taneleri, ışıldayan yıldızlar, parıldayan şimşekler).
- 🌙 **Gece / Gündüz Algılama (Day/Night Awareness):**
  - Open-Meteo `is_day` parametresi entegrasyonu ile saat gece olduğunda otomatik olarak devreye giren **Ay ve Yıldızlı Gece** arka plan teması.
- 📱 **Karanlık Tema (Dark Theme) Uyumluluğu:**
  - Karartma katmanı (scrim) ve yüksek kontrastlı tipografi ile hem aydınlık hem karanlık temada %100 okunabilirlik.
- 🎨 **Özel Uygulama İkonu:**
  - 1:1 oranında, köşeleri yumuşatılmış turkuaz zemin üzerine yerleştirilmiş özel güneş ve bulut vektör ikonu (`ic_launcher_airv`).
- 🔍 **Gerçek Zamanlı Şehir Arama:**
  - Yuvarlatılmış köşeli arama çubuğu (`SearchBarComponent`) ile anlık filtreleme.
- ⭐ **Kalıcı Favori Şehir Yönetimi (Room DB):**
  - Cihaz kapansa dahi korunan Room Veritabanı altyapısı.
  - Ana ekranda sarı çerçeveli, eklenme sırasına göre dinamik ölçeklenen **Favori Şehirler** alanı.
- 🚀 **İlk Açılış ve Yükleme Hızı Optimizasyonu:**
  - **Hafif Açılış İstekleri:** Ana ekranda 131 şehir için yalnızca anlık kart verileri çekilerek API yanıt boyutu %85 oranında küçültülmüştür.
  - **Talebe Bağlı Detay Yükleme (On-Demand Fetching):** Saatlik ve 7 günlük tahmin verileri yalnızca kullanıcı o şehrin detayına girdiğinde çekilir ve ViewModel belleğinde önbelleklenir.
- 📊 **Detaylı Hava Tahmin Ekranı:**
  - Hissedilen Sıcaklık, Nem %, Rüzgâr Hızı metrik kartı.
  - Yanal kaydırmalı **Saatlik Sıcaklık Tahminleri**.
  - Gri konteyner içerisinde **7 Günlük Hava Tahmin Listesi** (Yağış olasılığı, min/max sıcaklıklar, hava simgeleri).

---

## 🏗️ Mimari & Katman Haritası

Mimari tasarım ilkeleri, veri akışı ve katman sorumluluklarının detaylı analizi için **[docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)** rehberini inceleyebilirsiniz.

```
com.kampplus.hava
├── AirVApplication.kt / MainActivity.kt
├── core/
│   ├── common/        AppResult, AppError, ErrorMapper, WeatherCodeMapper, Dispatcher qualifiers (@IoDispatcher)
│   ├── database/      HavaDatabase (Room), DatabaseModule, FavoriteCityDao, FavoriteCityEntity, SettingsDao, SettingsEntity
│   ├── network/       NetworkModule (OkHttp, HttpLoggingInterceptor, Retrofit, Json)
│   ├── ui/            theme (Color, Theme, Type), LoadState (Loading, Content, Empty, Error)
│   └── navigation/    Screen (Type-safe routes: Home, Detail, Settings), HavaNavHost
├── data/
│   ├── local/         TurkishCityCatalog (81 İl), WorldCityCatalog (50 Metropol), CityCatalog
│   ├── remote/        OpenMeteoApi, ForecastResponse DTOs
│   └── repository/    WeatherRepositoryImpl, FavoriteCityRepositoryImpl, SettingsRepositoryImpl
├── domain/
│   ├── model/         CityItem, ScreenData, CityDetailData, HourlyForecast, DailyForecast, UserSettings
│   └── repository/    WeatherRepository, FavoriteCityRepository, SettingsRepository
└── presentation/
    ├── components/    ContentCard, SearchBarComponent, FavoriteCitiesSection,
    │                  WeatherMetricsCard, HourlyForecastSection, DailyForecastSection, AnimatedWeatherBackground
    ├── home/          HomeScreen
    ├── detail/        DetailScreen
    ├── settings/      SettingsScreen
    └── main/          WeatherViewModel
```

---

## 🔌 API — Open-Meteo (API Key Gerektirmez)

| Amaç | Çağrı | Açıklama |
|---|---|---|
| **Ana Ekran (Çoklu Şehir)** | `GET /v1/forecast?latitude=...&longitude=...&current=temperature_2m,weather_code,is_day&timezone=auto` | JSON Dizisi (Array). Ağ Yükü %85 Azaltılmış Hafif İstek. |
| **Detay Ekranı (Tek Şehir)** | `GET /v1/forecast?latitude=..&longitude=..&current=...&hourly=temperature_2m,weather_code&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max&timezone=auto` | JSON Nesnesi. Anlık, 24 saatlik ve 7 günlük detaylı tahmin. |

---

## 🧪 Derleme ve Test

```bash
./gradlew assembleDebug              # Debug APK derleme
./gradlew testDebugUnitTest          # Tüm birim testleri çalıştırma (10/10 Passed)
./gradlew assembleRelease            # Release APK derleme (R8 ve ProGuard aktif)
```

---

## 📜 Lisans & Kurallar

Proje geliştirmelerinde **Clean Architecture**, **MVVM**, **Clean Code** ve [RULES.md](app/RULES.md) kuralları %100 uygulanmıştır. Detaylı mimari belgeler için [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) dosyasını ziyaret edebilirsiniz.
