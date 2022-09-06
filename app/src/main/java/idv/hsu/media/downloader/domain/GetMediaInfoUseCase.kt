package idv.hsu.media.downloader.domain

import android.webkit.URLUtil
import idv.hsu.media.downloader.repository.SearchRecordRepository
import idv.hsu.media.downloader.repository.YtDlpRepository
import idv.hsu.media.downloader.vo.Result
import idv.hsu.media.downloader.vo.SearchRecord
import idv.hsu.media.downloader.vo.SearchType

class GetMediaInfoUseCase (
    private val repoYtDlp: YtDlpRepository,
    private val repoSearchRecord: SearchRecordRepository
) {
    suspend operator fun invoke(url: String, @SearchType searchType: Int): Result<String> {
        if (!URLUtil.isValidUrl(url)) {
            return Result.Error(Exception("Not a Valid url"))
        }
        return try {
            repoYtDlp.getMediaInfo(url)
            val result = repoSearchRecord.addSearchRecord(
                SearchRecord(
                    url,
                    System.currentTimeMillis(),
                    searchType
                )
            )
            if (result > 0) {
                Result.Success(url)
            } else {
                Result.Error(Exception("Unknown"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}