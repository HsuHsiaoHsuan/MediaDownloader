package idv.hsu.media.downloader.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import idv.hsu.media.downloader.repository.DownloadRecordRepository
import javax.inject.Inject

@HiltViewModel
class DownloadRecordViewModel @Inject constructor(
    repoDownloadRecord: DownloadRecordRepository
) : ViewModel() {

    val allDownloadRecord = repoDownloadRecord.allDownloadRecord
}