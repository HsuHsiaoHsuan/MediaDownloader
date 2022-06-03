package idv.hsu.media.downloader.vo

import android.os.Parcelable
import androidx.annotation.IntDef
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = "search_record_table")
data class SearchRecord(
    @PrimaryKey(autoGenerate = false)
    val url: String,
    @ColumnInfo(name = "search_time")
    val searchTime: Long,
    @ConvertState
    @ColumnInfo(name = "convert_state")
    var convertState: Int = CONVERT_STATE_INIT
) : Parcelable

@IntDef(
    CONVERT_STATE_FAIL,
    CONVERT_STATE_INIT,
    CONVERT_STATE_CONVERTING,
    CONVERT_STATE_DONE
)
@Retention(AnnotationRetention.SOURCE)
annotation class ConvertState

const val CONVERT_STATE_FAIL = -1
const val CONVERT_STATE_INIT = 0
const val CONVERT_STATE_CONVERTING = 1
const val CONVERT_STATE_DONE = 2