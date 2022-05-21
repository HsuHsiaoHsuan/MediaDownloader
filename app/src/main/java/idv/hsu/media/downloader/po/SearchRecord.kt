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
    val searchTime: Long
) : Parcelable