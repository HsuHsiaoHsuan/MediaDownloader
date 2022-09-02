package idv.hsu.media.downloader.domain

import idv.hsu.media.downloader.repository.SearchRecordRepository

class GetSearchRecordUseCase(
    repoSearchRecord: SearchRecordRepository
) {

    val searchAllData = repoSearchRecord.allSearch

    val searchByInput = repoSearchRecord.allSearchByInput

    val searchByWebView = repoSearchRecord.allSearchByWebView
}