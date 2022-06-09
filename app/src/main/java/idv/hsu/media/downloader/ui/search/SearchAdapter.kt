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
            holder.textSubtitle.text = holder.itemView.context.resources.getString(R.string.parsing)
            holder.buttonAction.isVisible = false
        } else {
            holder.imageCover.isVisible = true
            Glide.with(holder.itemView.context)
                .load(myVideoInfo.thumbnail)
                .centerCrop()
                .into(holder.imageCover)
            holder.textTitle.text = myVideoInfo.title
            holder.textSubtitle.text = myVideoInfo.uploader
            holder.buttonAction.isVisible = true
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

    inner class ViewHolder(binding: ItemSearchRecordBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        val imageCover = binding.imageCover
        val textTitle = binding.textTitle
        val textSubtitle = binding.textSubtitle
        val buttonAction = binding.buttonAction

        init {
            itemView.setOnClickListener(this)
            buttonAction.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            when (view) {
                itemView -> {
                    listener?.onItemClick(values[adapterPosition])
                }
                buttonAction -> {
                    listener?.onDownloadClick(values[adapterPosition])
                }
            }
        }
    }

    interface OnSearchRecordClickListener {
        fun onItemClick(data: SearchAndInfo)
        fun onDownloadClick(data: SearchAndInfo)
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