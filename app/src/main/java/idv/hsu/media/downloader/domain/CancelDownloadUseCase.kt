package idv.hsu.media.downloader.domain

import idv.hsu.media.downloader.repository.YtDlpRepository
import idv.hsu.media.downloader.worker.MediaType

class CancelDownloadUseCase(
    private val repoYtdlp: YtDlpRepository
) {
    suspend operator fun invoke(url: String, fileName: String, @MediaType type: Int) {
        repoYtdlp.cancelGetMedia(url, fileName, type)
    }
}