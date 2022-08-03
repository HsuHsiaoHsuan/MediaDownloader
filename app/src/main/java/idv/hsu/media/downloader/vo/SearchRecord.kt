package idv.hsu.media.downloader.vo

import android.os.Parcelable
import androidx.annotation.IntDef
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Keep
@Parcelize
@Serializable
@Entity(tableName = "search_record_table")
data class SearchRecord(
    @PrimaryKey(autoGenerate = false)
    val url: String,
    @ColumnInfo(name = "search_time")
    val searchTime: Long,
    @ColumnInfo(name = "search_type")
    val searchType: Int,
) : Parcelable

@IntDef(
    SEARCH_TYPE_TEXT_INPUT,
    SEARCH_TYPE_WEBVIEW,
    SEARCH_TYPE_CLIPBOARD
)
@Retention(AnnotationRetention.SOURCE)
annotation class SearchType

const val SEARCH_TYPE_TEXT_INPUT = 0
const val SEARCH_TYPE_WEBVIEW = 1
const val SEARCH_TYPE_CLIPBOARD = 2