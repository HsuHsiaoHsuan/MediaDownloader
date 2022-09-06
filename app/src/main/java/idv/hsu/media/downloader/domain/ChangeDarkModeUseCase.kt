package idv.hsu.media.downloader.domain

import idv.hsu.media.downloader.DataStorePreferencesManager
import kotlinx.coroutines.flow.Flow

class ChangeDarkModeUseCase(
    private val dataStorePrefManager: DataStorePreferencesManager
) {

    operator fun invoke(): Flow<Boolean?> {
        return dataStorePrefManager.darkMode
    }

    var darkMode: Flow<Boolean?> = dataStorePrefManager.darkMode

    suspend operator fun invoke(value: Boolean) {
        dataStorePrefManager.setDarkMode(value)
    }
}