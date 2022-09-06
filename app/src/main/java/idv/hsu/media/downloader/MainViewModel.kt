package idv.hsu.media.downloader

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import idv.hsu.media.downloader.domain.ChangeDarkModeUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    changeDarkModeUseCase: ChangeDarkModeUseCase
) : ViewModel() {

    fun writeFilePermission(context: Context) =
        ContextCompat.checkSelfPermission(
            context,
            WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    val darkMode: StateFlow<Boolean?> =
        changeDarkModeUseCase.darkMode.stateIn(viewModelScope, SharingStarted.Lazily, null)

}