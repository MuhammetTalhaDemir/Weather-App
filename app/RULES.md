# Android Weather App - Core Rules

## 1. Mimari ve Kod Kalitesi
- Projede kesinlikle **MVVM (Model-View-ViewModel)** ve Clean Architecture prensipleri kullanılacaktır.
- UI işlemleri, iş mantığı (business logic) ve veri çekme (data fetching) işlemleri birbirinden tamamen ayrılmalıdır.
- Kodlar "Clean Code" standartlarına uygun, isimlendirmeler açıklayıcı ve İngilizce olmalıdır.

## 2. Güvenlik
- API Anahtarları (API Keys) KESİNLİKLE kod içine (hardcoded) yazılmayacaktır.
- API Key yönetimi `local.properties` dosyası üzerinden yapılmalı ve `BuildConfig` ile koda çekilmelidir. `local.properties` dosyası `.gitignore` içinde olmalıdır.

## 3. Performans ve Optimizasyon
- İnternetten veri çekilirken JSON yanıtındaki tüm veriler değil, DTO (Data Transfer Object) kullanılarak sadece UI'da gösterilecek (sıcaklık, hava durumu, ikon vs.) veriler parse edilmelidir.
- Listelemeler için performansı yüksek yapılar (Jetpack Compose için LazyColumn, XML için RecyclerView) kullanılmalıdır.

## 4. Hata Yönetimi (Error Handling)
- İnternet olmaması, API'nin çökmesi, yanlış şehir aranması gibi tüm Edge-Case'ler ele alınmalıdır.
- Kullanıcıya anlamsız crash'ler yerine, UI üzerinde şık hata mesajları (Snackbar veya Error Screen) gösterilmelidir.

## 5. Veri Saklama
- Favori şehirler uygulamanın önbelleğinde (Cache) değil, kalıcı olarak cihazda saklanmalıdır. Bunun için **Room Database** veya **DataStore** kullanılmalıdır.

# Android Weather App - Technical Rules

1. **Teknoloji Yığını (Tech Stack):**
    - UI: Jetpack Compose
    - Asenkron İşlemler: Kotlin Coroutines & Flow (StateFlow)
    - Network: Retrofit2 & OkHttp (Interceptors eklenecek)
    - JSON Parsing: Kotlinx.serialization veya Gson
    - Local Storage: Room Database
    - Dependency Injection: Dagger Hilt

2. **Open-Meteo Spesifikasyonları:**
    - Open-Meteo API Key gerektirmez ancak `BASE_URL` değişkenleri `local.properties` üzerinden `BuildConfig`'e aktarılarak kullanılmalıdır.
    - API'den dönen `weather_code` değerleri için bir `WeatherCodeMapper` yazılmalı ve UI'da gösterilecek uygun ikon/metin karşılıkları buradan alınmalıdır.

3. **Compose UI Kuralları:**
    - Ekranlar (Screens) ve Bileşenler (Components) ayrı dosyalarda olmalıdır.
    - Compose fonksiyonları `Modifier` parametresini dışarıdan alacak şekilde tasarlanmalıdır.
    - UI State, ViewModel içinde `StateFlow` ile tutulmalı, UI sadece bu state'i observe etmelidir (collectAsState).
