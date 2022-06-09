package idv.hsu.media.downloader.ui.download

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import idv.hsu.media.downloader.databinding.ItemDownloadRecordBinding
import idv.hsu.media.downloader.db.relation.DownloadAndInfo
import idv.hsu.media.downloader.db.relation.SearchAndInfo
import idv.hsu.media.downloader.vo.*
import timber.log.Timber

class DownloadAdapter : RecyclerView.Adapter<DownloadAdapter.ViewHolder>() {
    private val values: MutableList<DownloadAndInfo> = mutableListOf()
    var listener: OnDownloadRecordClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDownloadRecordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.textTitle.text = item.myVideoInfo?.title ?: ""
        Glide.with(holder.itemView.context)
            .load(item.myVideoInfo?.thumbnail)
            .centerCrop()
            .into(holder.imageCover)
        when (item.download.downloadState) {
            DOWNLOAD_STATE_INIT,
            DOWNLOAD_STATE_PARSING-> {
                holder.progress.isIndeterminate = true
                holder.progress.isVisible = true
                holder.textSubtitle.text = ""
            }
            DOWNLOAD_STATE_DOWNLOADING -> {
                holder.progress.setProgressCompat(item.download.downloadProgress.toInt(), true)
                holder.progress.isVisible = true
                holder.textSubtitle.text = ""
            }
            DOWNLOAD_STATE_DONE,
            DOWNLOAD_STATE_FAIL-> {
                holder.progress.isVisible = false
                holder.textSubtitle.text = item.myVideoInfo?.uploader ?: ""
            }
        }
    }

    override fun getItemCount(): Int = values.size

    fun setData(data: List<DownloadAndInfo>) {
        val diffCallback = DownloadDiffCallback(values.toList(), data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        values.clear()
        values.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(binding: ItemDownloadRecordBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val imageCover = binding.imageCover
        val textTitle = binding.textTitle
        val textSubtitle = binding.textSubtitle
        val progress = binding.progressHorizontal
        val buttonAction = binding.buttonAction

        init {
            itemView.setOnClickListener(this)
            buttonAction.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
        }
    }

    interface OnDownloadRecordClickListener {
        fun onItemClick(data: SearchAndInfo)
        fun onActionClick(data: SearchAndInfo)
    }
}

class DownloadDiffCallback(
    private val oldList: List<DownloadAndInfo>,
    private val newList: List<DownloadAndInfo>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].download.url == newList[newItemPosition].download.url &&
                oldList[oldItemPosition].download.fileName == newList[newItemPosition].download.fileName &&
                oldList[oldItemPosition].download.fileExtension == newList[newItemPosition].download.fileExtension
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}