package idv.hsu.media.downloader.domain

import idv.hsu.media.downloader.repository.DownloadRecordRepository
import idv.hsu.media.downloader.repository.YtDlpRepository
import idv.hsu.media.downloader.vo.DOWNLOAD_STATE_PAUSED
import idv.hsu.media.downloader.vo.DownloadRecord

class PauseDownloadUseCase(
    private val repoDownloadRecord: DownloadRecordRepository,
    private val repoTyDlp: YtDlpRepository
) {
    suspend operator fun invoke(record: DownloadRecord) {
        repoDownloadRecord.updateDownloadRecord(record.apply {
            downloadState = DOWNLOAD_STATE_PAUSED
        })
        val id = "${record.url}_${record.fileExtension}"
        repoTyDlp.killYtdlpProcess(id)
    }
}