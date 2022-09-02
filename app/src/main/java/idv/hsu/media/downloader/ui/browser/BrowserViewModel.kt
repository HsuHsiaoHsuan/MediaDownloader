package idv.hsu.media.downloader.ui.browser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import idv.hsu.media.downloader.domain.GetMediaInfoUseCase
import idv.hsu.media.downloader.vo.SEARCH_TYPE_WEBVIEW
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowserViewModel @Inject constructor(
    private val getMediaInfoUseCase: GetMediaInfoUseCase
) : ViewModel() {

    fun getMediaInfo(url: String) {
        viewModelScope.launch {
            getMediaInfoUseCase(url, SEARCH_TYPE_WEBVIEW)
        }
    }
}