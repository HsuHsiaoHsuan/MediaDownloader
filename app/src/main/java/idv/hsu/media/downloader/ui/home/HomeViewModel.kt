package idv.hsu.media.downloader.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import idv.hsu.media.downloader.db.relation.SearchAndInfo
import idv.hsu.media.downloader.domain.DeleteSearchRecordUseCase
import idv.hsu.media.downloader.domain.GetMediaInfoUseCase
import idv.hsu.media.downloader.domain.GetSearchRecordUseCase
import idv.hsu.media.downloader.vo.Result
import idv.hsu.media.downloader.vo.SEARCH_TYPE_TEXT_INPUT
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMediaInfoUseCase: GetMediaInfoUseCase,
    getSearchRecordUseCase: GetSearchRecordUseCase,
    private val delSearchRecordUseCase: DeleteSearchRecordUseCase
) : ViewModel() {

    private val _uiState = MutableSharedFlow<HomeUiState>()
    val uiState: SharedFlow<HomeUiState> = _uiState

    val searchAllData = getSearchRecordUseCase.searchAllData

    fun getMediaInfo(url: String) {
        viewModelScope.launch {
            getMediaInfoUseCase(url, SEARCH_TYPE_TEXT_INPUT).let { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.emit(HomeUiState.ParseAdded(url))
                    }
                    is Result.Error -> {
                        _uiState.emit(HomeUiState.ParseNoteAdded(url, result.exception.toString()))
                    }
                    Result.Loading -> Unit
                }
            }
        }
    }

    fun delSearchRecord(record: SearchAndInfo) {
        viewModelScope.launch {
            delSearchRecordUseCase(record)
        }
    }
}

sealed class HomeUiState {
    data class ParseAdded(val url: String) : HomeUiState()
    data class ParseNoteAdded(val url: String, val msg: String) : HomeUiState()
}