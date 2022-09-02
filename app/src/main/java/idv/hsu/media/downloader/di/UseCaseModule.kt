package idv.hsu.media.downloader.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import idv.hsu.media.downloader.DataStorePreferencesManager
import idv.hsu.media.downloader.domain.*
import idv.hsu.media.downloader.repository.DownloadRecordRepository
import idv.hsu.media.downloader.repository.MyVideoInfoRepository
import idv.hsu.media.downloader.repository.SearchRecordRepository
import idv.hsu.media.downloader.repository.YtDlpRepository

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    fun provideGetMediaInfoUseCase(
        repoYtDlp: YtDlpRepository,
        repoSearchRecord: SearchRecordRepository
    ): GetMediaInfoUseCase {
        return GetMediaInfoUseCase(repoYtDlp, repoSearchRecord)
    }

    @Provides
    fun provideGetSearchRecordUseCase(
        repoSearchRecord: SearchRecordRepository
    ): GetSearchRecordUseCase {
        return GetSearchRecordUseCase(repoSearchRecord)
    }

    @Provides
    fun provideDeleteSearchRecordUseCase(
        repoSearchRecord: SearchRecordRepository,
        repoMyVideoInfo: MyVideoInfoRepository
    ): DeleteSearchRecordUseCase {
        return DeleteSearchRecordUseCase(repoSearchRecord, repoMyVideoInfo)
    }

    @Provides
    fun provideGetDownloadRecordUseCase(
        repoDownloadRecord: DownloadRecordRepository
    ): GetDownloadRecordUseCase {
        return GetDownloadRecordUseCase(repoDownloadRecord)
    }

    @Provides
    fun provideDeleteDownloadRecordUseCase(
        repoDownloadRecord: DownloadRecordRepository
    ): DeleteDownloadRecordUseCase {
        return DeleteDownloadRecordUseCase(repoDownloadRecord)
    }

    @Provides
    fun provideDownloadMediaUseCase(
        repoTyDlp: YtDlpRepository
    ): DownloadMediaUseCase {
        return DownloadMediaUseCase(repoTyDlp)
    }

    @Provides
    fun providePauseDownloadUseCase(
        repoDownloadRecord: DownloadRecordRepository,
        repoTyDlp: YtDlpRepository
    ): PauseDownloadUseCase {
        return PauseDownloadUseCase(repoDownloadRecord, repoTyDlp)
    }

    @Provides
    fun provideCancelDownloadUseCase(
        repoTyDlp: YtDlpRepository
    ): CancelDownloadUseCase {
        return CancelDownloadUseCase(repoTyDlp)
    }

    @Provides
    fun provideChangeDarkModeUseCase(
        dataStorePreferencesManager: DataStorePreferencesManager
    ): ChangeDarkModeUseCase {
        return ChangeDarkModeUseCase(dataStorePreferencesManager)
    }
}