package idv.hsu.media.downloader.repository

import idv.hsu.media.downloader.db.SearchRecordDao
import idv.hsu.media.downloader.db.relation.SearchAndInfo
import idv.hsu.media.downloader.vo.SEARCH_TYPE_TEXT_INPUT
import idv.hsu.media.downloader.vo.SEARCH_TYPE_WEBVIEW
import idv.hsu.media.downloader.vo.SearchRecord
import idv.hsu.media.downloader.vo.SearchType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRecordRepository @Inject constructor(
    private val searchDao: SearchRecordDao
) {
    val allSearchByInput: Flow<List<SearchAndInfo>> = searchDao.getSearchAndInfoByType(
        SEARCH_TYPE_TEXT_INPUT)

    val allSearchByWebview: Flow<List<SearchAndInfo>> = searchDao.getSearchAndInfoByType(
        SEARCH_TYPE_WEBVIEW
    )

    suspend fun addSearchRecord(record: SearchRecord) = withContext(Dispatchers.IO) {
        searchDao.addSearchRecord(record)
    }

    suspend fun delSearchRecord(record: SearchRecord) = withContext(Dispatchers.IO) {
        searchDao.deleteSearchRecord(record)
    }

    suspend fun clearSearchRecord(@SearchType searchType: Int) = withContext(Dispatchers.IO) {
        searchDao.clear()
    }
}