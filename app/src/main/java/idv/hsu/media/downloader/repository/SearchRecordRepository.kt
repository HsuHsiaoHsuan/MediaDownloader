package idv.hsu.media.downloader.repository

import idv.hsu.media.downloader.db.SearchRecordDao
import idv.hsu.media.downloader.db.relation.SearchAndInfo
import idv.hsu.media.downloader.po.SearchRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRecordRepository @Inject constructor(
    private val searchDao: SearchRecordDao
) {
    val allSearchRecord: Flow<List<SearchRecord>> = searchDao.getAllSearchRecord()

    val allSearchAndInfo: Flow<List<SearchAndInfo>> = searchDao.getAllSearchAndInfo()

    suspend fun addSearchRecord(record: SearchRecord) = withContext(Dispatchers.IO) {
        searchDao.addSearchRecord(record)
    }

    suspend fun delSearchRecord(record: SearchRecord) = withContext(Dispatchers.IO) {
        searchDao.deleteSearchRecord(record)
    }

    suspend fun clearSearchRecord() = withContext(Dispatchers.IO) {
        searchDao.clear()
    }
}