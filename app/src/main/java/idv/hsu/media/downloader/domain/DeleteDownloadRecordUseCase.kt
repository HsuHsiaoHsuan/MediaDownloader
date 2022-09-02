package idv.hsu.media.downloader.domain

import idv.hsu.media.downloader.repository.DownloadRecordRepository
import idv.hsu.media.downloader.vo.DownloadRecord

class DeleteDownloadRecordUseCase(
    private val repoDownloadRecord: DownloadRecordRepository
) {
    suspend operator fun invoke(record: DownloadRecord) {
        repoDownloadRecord.deleteDownloadRecord(record)
    }
}