package idv.hsu.media.downloader.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import idv.hsu.media.downloader.repository.SearchRecordRepository
import idv.hsu.media.downloader.po.SearchRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchRecordViewModel @Inject constructor(
    private val repoSearch: SearchRecordRepository
) : ViewModel() {

    val allSearchRecord = repoSearch.allSearchRecord

    val allSearchAndInfo = repoSearch.allSearchAndInfo

    private val _uiState = MutableSharedFlow<SearchRecordUiState>()
    val uiState: SharedFlow<SearchRecordUiState> = _uiState

    fun addSearch(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repoSearch.addSearchRecord(SearchRecord(url, System.currentTimeMillis()))
                if (result > 0) {
                    _uiState.emit(SearchRecordUiState.AddSearchOk(url))
                } else {
                    _uiState.emit(SearchRecordUiState.AddSearchNone(url))
                }
            } catch (e: Exception) {
                _uiState.emit(SearchRecordUiState.AddSearchFail(url, e.toString()))
            }
        }
    }
}

sealed class SearchRecordUiState {
    data class AddSearchOk(val url: String) : SearchRecordUiState()
    data class AddSearchNone(val url: String) : SearchRecordUiState()
    data class AddSearchFail(val url: String, val message: String) : SearchRecordUiState()
}