package idv.hsu.media.downloader.db.relation

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Relation
import idv.hsu.media.downloader.po.SearchRecord
import idv.hsu.media.downloader.vo.MyVideoInfo
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class SearchAndInfo(
    @Embedded val search: SearchRecord,
    @Relation(
        parentColumn = "url",
        entityColumn = "url"
    )
    val myVideoInfo: MyVideoInfo?
): Parcelable