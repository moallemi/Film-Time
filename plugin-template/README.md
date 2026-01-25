# FilmTime Plugin Development Guide

This directory contains sample plugins and documentation for creating FilmTime stream plugins.

## Overview

FilmTime plugins are standalone Android apps that provide stream sources for movies and TV shows. The host app discovers plugins via Android's `PackageManager` and communicates with them through `ContentProvider`.

## Sample Plugins

| Directory | Description |
|-----------|-------------|
| `stream-basic-sample/` | Simple stream plugin without authentication |
| `stream-auth-sample/` | Stream plugin with login/authentication flow |
| `embed-sample/` | Embed plugin that returns URLs for WebView playback |

## Quick Start

1. Copy one of the sample directories as your starting point
2. Update `applicationId` in `app/build.gradle.kts`
3. Update the `authority` in your provider (must match `io.filmtime.plugin.<your-id>`)
4. Implement your stream fetching logic in the `ContentProvider`
5. Build and install: `./gradlew assembleDebug && adb install app/build/outputs/apk/debug/app-debug.apk`

## Plugin Architecture

### Required Components

1. **Discovery Service** - Empty service with intent filter for discovery
2. **Content Provider** - Handles metadata and stream queries
3. **Login Activity** (optional) - For plugins requiring authentication

### AndroidManifest.xml

```xml
<!-- Discovery service -->
<service
    android:name=".PluginDiscoveryService"
    android:exported="true">
    <intent-filter>
        <action android:name="io.filmtime.plugin.STREAM_PROVIDER" />
    </intent-filter>
</service>

<!-- Content provider -->
<provider
    android:name=".YourPluginProvider"
    android:authorities="io.filmtime.plugin.your-plugin-id"
    android:exported="true" />

<!-- Login activity (if auth required) -->
<activity
    android:name=".LoginActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="io.filmtime.plugin.ACTION_LOGIN" />
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</activity>
```

## Content Provider Implementation

### URI Patterns

| URI | Purpose |
|-----|---------|
| `content://<authority>/metadata` | Plugin information |
| `content://<authority>/stream/movie?tmdb_id=...` | Movie streams |
| `content://<authority>/stream/show?tmdb_id=...&season=...&episode=...` | TV show streams |

### Metadata Query

Return a cursor with plugin information:

```kotlin
private fun queryMetadata(): Cursor {
    val cursor = MatrixCursor(arrayOf(
        "plugin_id",      // Unique plugin identifier
        "name",           // Display name
        "description",    // Plugin description
        "version",        // Version string
        "icon_url",       // Optional icon URL
        "requires_auth",  // 1 if auth required, 0 otherwise
        "login_activity", // Full class name of login activity (if auth required)
    ))
    cursor.addRow(arrayOf(
        "my-plugin",
        "My Plugin",
        "A custom stream plugin",
        "1.0.0",
        "",
        0,  // No auth required
        "",
    ))
    return cursor
}
```

### Stream Query

Return available streams for a movie or TV show:

```kotlin
private fun queryStream(uri: Uri): Cursor {
    // Get query parameters
    val tmdbId = uri.getQueryParameter("tmdb_id")
    val title = uri.getQueryParameter("title")
    val year = uri.getQueryParameter("year")
    val imdbId = uri.getQueryParameter("imdb_id")

    // For TV shows only
    val season = uri.getQueryParameter("season")
    val episode = uri.getQueryParameter("episode")

    val cursor = MatrixCursor(arrayOf(
        "stream_url",   // Stream URL
        "quality",      // auto, sd, hd, fhd, uhd
        "stream_type",  // hls, dash, mp4
        "title",        // Stream title/label
        "headers",      // JSON object of HTTP headers
        "subtitles",    // JSON array of subtitle objects
    ))

    // Add your streams
    cursor.addRow(arrayOf(
        "https://example.com/stream.m3u8",
        "hd",
        "hls",
        "HD Stream",
        """{"User-Agent": "MyPlugin/1.0"}""",
        """[{"url": "https://example.com/subs.srt", "language": "en", "label": "English"}]""",
    ))

    return cursor
}
```

### Headers Format

JSON object with header name-value pairs:

```json
{
    "User-Agent": "MyPlugin/1.0",
    "Authorization": "Bearer token123",
    "X-Custom-Header": "value"
}
```

### Subtitles Format

JSON array of subtitle objects:

```json
[
    {
        "url": "https://example.com/subtitles.srt",
        "language": "en",
        "label": "English"
    },
    {
        "url": "https://example.com/subtitles-es.vtt",
        "language": "es",
        "label": "Spanish"
    }
]
```

Supported subtitle formats: `.srt`, `.vtt`, `.ass`, `.ssa`, `.ttml`

## Authentication

For plugins requiring login:

### 1. Set `requires_auth = 1` in metadata

### 2. Implement `call()` method for auth state

```kotlin
override fun call(method: String, arg: String?, extras: Bundle?): Bundle? {
    return when (method) {
        "get_auth_state" -> Bundle().apply {
            putBoolean("requires_auth", true)
            putBoolean("is_authenticated", isLoggedIn())
            if (isLoggedIn()) {
                putString("auth_display_name", getUsername())
            }
        }
        "logout" -> {
            clearCredentials()
            Bundle().apply {
                putBoolean("logout_success", true)
            }
        }
        else -> null
    }
}
```

### 3. Implement Login Activity

```kotlin
class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Show login UI
    }

    private fun onLoginSuccess() {
        setResult(RESULT_OK, Intent().apply {
            putExtra("login_result", 1)  // SUCCESS
        })
        finish()
    }

    private fun onLoginCancelled() {
        setResult(RESULT_CANCELED, Intent().apply {
            putExtra("login_result", 2)  // CANCELLED
        })
        finish()
    }
}
```

### 4. Check auth in stream query

```kotlin
private fun queryStream(uri: Uri): Cursor {
    val cursor = MatrixCursor(...)

    if (!isLoggedIn()) {
        cursor.extras = Bundle().apply {
            putBoolean("auth_required", true)
        }
        return cursor  // Return empty cursor with auth_required flag
    }

    // Return streams...
}
```

## Stream Types

| Type | Description | Example |
|------|-------------|---------|
| `hls` | HTTP Live Streaming | `.m3u8` playlists |
| `dash` | MPEG-DASH | `.mpd` manifests |
| `mp4` | Direct MP4 | `.mp4` files |
| `embed` | WebView embed URL | Opens in browser/WebView |

## Embed Plugins

Embed plugins return URLs that should be opened in a WebView instead of being played directly in ExoPlayer. This is useful for services that provide iframe/embed players.

### How It Works

1. Set `stream_type` to `embed` instead of `hls`, `dash`, or `mp4`
2. The `stream_url` should be the embed URL (e.g., `https://foo.com/embed/movie/123`)
3. FilmTime will open this URL in a Custom Tabs browser instead of the native player

### Example

```kotlin
private fun queryMovieEmbed(uri: Uri): Cursor {
    val cursor = MatrixCursor(arrayOf(
        "stream_url",
        "quality",
        "stream_type",
        "title",
        "headers",
        "subtitles",
    ))

    val tmdbId = uri.getQueryParameter("tmdb_id")

    cursor.addRow(arrayOf(
        "https://foo.com/embed/movie/$tmdbId",
        "auto",
        "embed",  // This tells the app to open in WebView
        "Watch Movie",
        "",       // No headers needed
        "",       // No subtitles - handled by embed player
    ))

    return cursor
}
```

### For TV Shows

Include season and episode in the embed URL:

```kotlin
val embedUrl = "https://foo.com/embed/tv/$tmdbId/$season/$episode"
```

### When to Use Embed

- When a service only provides iframe/embed players
- When the stream URL requires JavaScript to resolve
- When the service handles its own player UI and subtitles

## Quality Values

| Value | Description |
|-------|-------------|
| `auto` | Adaptive quality |
| `sd` | Standard definition (480p) |
| `hd` | High definition (720p) |
| `fhd` | Full HD (1080p) |
| `uhd` | Ultra HD (4K) |

## Testing

1. Build and install your plugin APK
2. Open FilmTime app
3. Go to Plugin Manager to verify your plugin appears
4. Try playing content to test stream fetching
5. For auth plugins, verify login flow works correctly

## Troubleshooting

- **Plugin not discovered**: Verify the service intent filter action is exactly `io.filmtime.plugin.STREAM_PROVIDER`
- **Streams not loading**: Check that the provider authority matches `io.filmtime.plugin.<id>` pattern
- **Auth not working**: Ensure login activity has the correct intent filter and returns proper result codes
