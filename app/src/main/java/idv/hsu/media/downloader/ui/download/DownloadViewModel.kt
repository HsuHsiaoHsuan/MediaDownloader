package idv.hsu.media.downloader.ui.download

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import idv.hsu.media.downloader.domain.GetDownloadRecordUseCase
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val downloadRecordUseCase: GetDownloadRecordUseCase
) : ViewModel() {

    val downloadAudio = downloadRecordUseCase.downloadAudio

    val downloadVideo = downloadRecordUseCase.downloadVideo
}