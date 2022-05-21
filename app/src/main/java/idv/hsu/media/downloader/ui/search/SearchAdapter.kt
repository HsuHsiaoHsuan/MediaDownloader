package idv.hsu.media.downloader.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import idv.hsu.media.downloader.databinding.ItemSearchBinding
import idv.hsu.media.downloader.po.SearchRecord

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    private val values: MutableList<SearchRecord> = mutableListOf()
    var listener: OnSearchClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.textUrl.text = item.url
    }

    override fun getItemCount(): Int = values.size

    fun setData(data: List<SearchRecord>) {
        val diffCallback = SearchDiffCallback(values.toList(), data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        values.clear()
        values.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(binding: ItemSearchBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        val textUrl = binding.textUrl

        override fun onClick(view: View?) {
            when (view) {
                itemView -> {

                }
            }
        }
    }

    interface OnSearchClickListener {
        fun onItemClick(data: SearchRecord)
    }
}

class SearchDiffCallback(
    private val oldList: List<SearchRecord>,
    private val newList: List<SearchRecord>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].url == newList[newItemPosition].url
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}