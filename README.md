<h1 align="center">Giphy Search</h1>

**Giphy Search** is an application for searching and exploring GIF animations powered by Giphy API. The project is built with a strong focus on Clean Architecture, smooth UX, and robust fault tolerance.

### 📱 Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/8df6249c-03f4-4806-ae12-c754d8f5e6c5" width="200" />
  <img src="https://github.com/user-attachments/assets/a16eb63d-87d2-46cd-9c80-db9ce8b40ea5" width="200" />
  <img src="https://github.com/user-attachments/assets/41fd7be9-21eb-4951-ae63-744cfd3908e7" width="200" />
</p>

### ✨ Requirements
* **OS:** Android 10 or higher
* **SDK:** API Level 29+

### 🚀 Features
* **Seamless Scrolling (Pre-fetching):** Pagination is triggered 10 items before reaching the end of the list. On a stable connection, the user enjoys an infinite scroll without ever seeing a loading spinner.
* **Dynamic Grid (Staggered Grid):** Image cards pre-calculate their aspectRatio directly from the API response. This completely eliminates layout shifts while images are loading.
* **Custom Shimmer Effect:** A custom-built Jetpack Compose shimmer animation (zero third-party libraries) that perfectly matches the shape and size of the upcoming GIF.
* **Fault Tolerance:** Features error states on startup and graceful Retry buttons for network drops during pagination or full-screen GIF viewing.
* **Adaptability:** The UI automatically adapts to system theme changes (Day/Night mode), including a fully adaptive system Splash Screen.

### 🛠 Tech Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Architecture:** Clean Architecture + MVVM + UDF (Unidirectional Data Flow)
- **Concurrency:** Kotlin Coroutines & Flow (StateFlow)
- **Network:** Retrofit 2 + OkHttp3 (Custom Interceptor for API key injection)
- **JSON Parsing:** kotlinx.serialization
- **Image Loading:** Coil 3 (with Crossfade and onLoading/onError state handling)
- **Dependency Injection:** Dagger Hilt
- **Testing:** JUnit 4 + MockK + kotlinx-coroutines-test
- **Build & Security:** Gradle Kotlin DSL, configured ProGuard/R8 with DTO protection via @Keep.

### 📥 Build App

1. **Clone the repository:**
   ```bash
   git clone https://github.com/kolisnichenko2828/Giphy-Search.git
   ```
2. **Open the project in Android Studio.**
3. **Add API Key:**
   * Get your key from [Giphy API](https://developers.giphy.com).
   * Add it to your local.properties file:
   ```properties
   GIPHY_API=YOUR_API_KEY
   ```
4. **Build the app or run it on device.**
