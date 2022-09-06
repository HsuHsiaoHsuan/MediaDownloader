package idv.hsu.media.downloader

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("media_downloader")

@Singleton
class DataStorePreferencesManager @Inject constructor(
    @ApplicationContext appContext: Context
) {
    private val dataStore = appContext.dataStore


    val darkMode: Flow<Boolean> = dataStore.data
        .catch {
            emit(emptyPreferences())
        }.map { pref ->
            val key =
                booleanPreferencesKey(KEY_DARK_MODE)
            pref[key] ?: false
        }

    suspend fun setDarkMode(value: Boolean) {
        val darkMode = booleanPreferencesKey(KEY_DARK_MODE)
        dataStore.edit { config ->
            config[darkMode] = value
        }
    }

    companion object {
        private const val KEY_DARK_MODE = "dark_mode"
    }
}

// ref:
// https://stackoverflow.com/questions/65252533/provide-preferences-datastore-with-hilt
// https://developer.android.com/topic/libraries/architecture/datastore#kotlin