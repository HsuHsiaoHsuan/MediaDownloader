package idv.hsu.media.downloader.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import idv.hsu.media.downloader.R
import idv.hsu.media.downloader.databinding.ItemSearchRecordBinding
import idv.hsu.media.downloader.db.relation.SearchAndInfo
import idv.hsu.media.downloader.vo.CONVERT_STATE_CONVERTING
import idv.hsu.media.downloader.vo.CONVERT_STATE_DONE
import idv.hsu.media.downloader.vo.CONVERT_STATE_FAIL

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private val values: MutableList<SearchAndInfo> = mutableListOf()
    var listener: OnSearchRecordClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchRecordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val myVideoInfo = item.myVideoInfo
        if (myVideoInfo == null) {
            holder.imageCover.isVisible = false
            holder.textTitle.text = item.search.url
            holder.textSubtitle.text = "click button to refresh"
            with(holder.buttonAction) {
                isVisible = true
                setImageResource(R.drawable.ic_refresh_24)
                setOnClickListener {
                    // need show refresh and delete
                    listener?.onActionRefreshClick(item)
                }
            }
        } else {
            var title = item.search.url
            var subtitle = ""
            when (item.myVideoInfo.convertState) {
                CONVERT_STATE_FAIL -> {
                    subtitle =
                        holder.itemView.context.resources.getString(R.string.parsing_process_failed)
                    with(holder.buttonAction) {
                        isVisible = true
                        setImageResource(R.drawable.ic_delete_24)
                        setOnClickListener {
                            listener?.onActionDeleteClick(item)
                        }
                    }
                }
                CONVERT_STATE_CONVERTING -> {
                    holder.imageCover.isVisible = false
                    subtitle = holder.itemView.context.resources.getString(R.string.parsing)
                    holder.buttonAction.isVisible = false
                    R.drawable.ic_cancel_24
                }
                CONVERT_STATE_DONE -> {
                    holder.imageCover.isVisible = true
                    Glide.with(holder.itemView.context)
                        .load(myVideoInfo.thumbnail)
                        .centerCrop()
                        .into(holder.imageCover)
                    title = item.myVideoInfo.title
                    subtitle = item.myVideoInfo.uploader
                    with(holder.buttonAction) {
                        holder.buttonAction.isVisible = true
                        setImageResource(R.drawable.ic_more_24)
                        // FIXME should be more
                        setOnClickListener {
                            listener?.onActionMoreClick(item, this)
                        }
                    }
                }
                else -> {
                    holder.buttonAction.isVisible = false
                }
            }
            holder.textTitle.text = title
            holder.textSubtitle.text = subtitle
        }
    }

    override fun getItemCount(): Int = values.size

    fun setData(data: List<SearchAndInfo>) {
        val diffCallback = SearchDiffCallback(values.toList(), data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        values.clear()
        values.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(binding: ItemSearchRecordBinding) :
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        val imageCover = binding.imageCover
        val textTitle = binding.textTitle
        val textSubtitle = binding.textSubtitle
        val buttonAction = binding.buttonAction

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            when (view) {
                itemView -> {
                    listener?.onItemClick(values[adapterPosition])
                }
            }
        }
    }

    interface OnSearchRecordClickListener {
        fun onItemClick(data: SearchAndInfo)
        fun onActionDownloadClick(data: SearchAndInfo, view: View)
        fun onActionDeleteClick(data: SearchAndInfo)
        fun onActionRefreshClick(data: SearchAndInfo)
        fun onActionMoreClick(data: SearchAndInfo, view: View)
    }
}

class SearchDiffCallback(
    private val oldList: List<SearchAndInfo>,
    private val newList: List<SearchAndInfo>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].search.url == newList[newItemPosition].search.url
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}