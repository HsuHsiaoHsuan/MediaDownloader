package idv.hsu.media.downloader.utils

import androidx.appcompat.app.AppCompatDelegate

fun changeDarkMode(isDarkMode: Boolean) {
    AppCompatDelegate.setDefaultNightMode(
        if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
    )
}