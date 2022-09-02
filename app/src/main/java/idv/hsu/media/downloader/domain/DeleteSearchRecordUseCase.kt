package idv.hsu.media.downloader.domain

import idv.hsu.media.downloader.db.relation.SearchAndInfo
import idv.hsu.media.downloader.repository.MyVideoInfoRepository
import idv.hsu.media.downloader.repository.SearchRecordRepository

class DeleteSearchRecordUseCase(
    private val repoSearchRecord: SearchRecordRepository,
    private val repoMyVideoInfo: MyVideoInfoRepository
) {
    suspend operator fun invoke(record: SearchAndInfo) {
        repoSearchRecord.delSearchRecord(record.search)
        record.myVideoInfo?.also {
            repoMyVideoInfo.delMyVideoInfo(it)
        }
    }
}