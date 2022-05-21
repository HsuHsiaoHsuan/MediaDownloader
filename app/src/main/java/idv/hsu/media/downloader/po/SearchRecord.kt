package idv.hsu.media.downloader.po

import android.os.Parcelable
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
    @ColumnInfo(name = "convert_state") // 0: nothing, 1: converting, -1: fail, 2: done
    var convertState: Int = 0
) : Parcelable {
    companion object {
        const val STATE_FAIL = -1
        const val STATE_INIT = 0
        const val STATE_CONVERTING = 1
        const val STATE_DONE = 2
    }
}
