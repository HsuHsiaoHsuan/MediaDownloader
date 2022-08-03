package idv.hsu.media.downloader.db.relation

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Relation
import idv.hsu.media.downloader.vo.MyVideoInfo
import idv.hsu.media.downloader.vo.SearchRecord
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Keep
@Parcelize
@Serializable
data class SearchAndInfo(
    @Embedded val search: SearchRecord,
    @Relation(
        parentColumn = "url",
        entityColumn = "url"
    )
    val myVideoInfo: MyVideoInfo?
) : Parcelable