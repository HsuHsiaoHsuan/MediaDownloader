package idv.hsu.media.downloader.db.relation

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Relation
import idv.hsu.media.downloader.vo.DownloadRecord
import idv.hsu.media.downloader.vo.MyVideoInfo
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DownloadAndInfo(
    @Embedded val download: DownloadRecord,
    @Relation(
        parentColumn = "url",
        entityColumn = "url"
    )
    val myVideoInfo: MyVideoInfo?
) : Parcelable