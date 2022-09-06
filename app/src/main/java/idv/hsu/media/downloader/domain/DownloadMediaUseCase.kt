package idv.hsu.media.downloader.domain

import idv.hsu.media.downloader.repository.YtDlpRepository
import idv.hsu.media.downloader.worker.MediaType

class DownloadMediaUseCase(
    private val repoYtdlp: YtDlpRepository
) {
    suspend operator fun invoke(url: String, fileName: String, @MediaType type: Int) {
        repoYtdlp.getMedia(url, fileName, type)
    }
}