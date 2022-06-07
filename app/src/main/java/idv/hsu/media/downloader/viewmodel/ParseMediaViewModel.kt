package idv.hsu.media.downloader.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import idv.hsu.media.downloader.repository.MyVideoInfoRepository
import idv.hsu.media.downloader.repository.YtDlpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParseMediaViewModel @Inject constructor(
    private val repoYtdlp: YtDlpRepository,
    private val repoMyVideoInfo: MyVideoInfoRepository
) : ViewModel() {

    private val _uiState = MutableSharedFlow<ParseMediaUiState>()
    val uiState: SharedFlow<ParseMediaUiState> = _uiState

    fun getVideoInfo(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(ParseMediaUiState.ParsingUrl(url))
            try {
                val videoInfo = repoYtdlp.getMediaInfo(url)
                if (videoInfo != null) {
                    _uiState.emit(ParseMediaUiState.ParseOk(url))
                } else {
                    _uiState.emit(ParseMediaUiState.ParseFail(url, "null data"))
                }
                _uiState.emit((ParseMediaUiState.ParseOk(url)))
            } catch (e: Exception) {
                _uiState.emit(ParseMediaUiState.ParseFail(url, e.toString()))
            }
        }
    }
}

sealed class ParseMediaUiState {
    data class ParsingUrl(val url: String) : ParseMediaUiState()
//    data class ParseOk(val url: String, val videoInfo: VideoInfo) : ParseMediaUiState()
    data class ParseOk(val url: String) : ParseMediaUiState()
    data class ParseFail(val url: String, val message: String) : ParseMediaUiState()
}