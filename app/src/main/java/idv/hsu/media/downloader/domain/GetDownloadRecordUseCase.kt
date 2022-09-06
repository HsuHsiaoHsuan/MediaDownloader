package idv.hsu.media.downloader.domain

import idv.hsu.media.downloader.repository.DownloadRecordRepository

class GetDownloadRecordUseCase(
    repoDownloadRecord: DownloadRecordRepository
) {
    val downloadAll = repoDownloadRecord.allDownloadRecord

    val downloadAudio = repoDownloadRecord.allAudioRecord

    val downloadVideo = repoDownloadRecord.allVideoRecord
}