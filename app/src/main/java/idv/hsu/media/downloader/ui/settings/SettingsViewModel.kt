package idv.hsu.media.downloader.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import idv.hsu.media.downloader.domain.ChangeDarkModeUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val changeDarkModeUseCase: ChangeDarkModeUseCase
) : ViewModel() {

    val darkMode: StateFlow<Boolean?> =
        changeDarkModeUseCase.darkMode.stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun setDarkMode(value: Boolean) = viewModelScope.launch {
        changeDarkModeUseCase.invoke(value)
    }
}