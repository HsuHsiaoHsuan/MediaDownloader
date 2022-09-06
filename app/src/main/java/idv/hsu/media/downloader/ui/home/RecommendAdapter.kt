package idv.hsu.media.downloader.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import idv.hsu.media.downloader.databinding.ItemRecommendBinding

class RecommendAdapter(
    private val values: List<Triple<String, String, Int>>,
    private val listener: OnRecommendClickListener
) :
    RecyclerView.Adapter<RecommendAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecommendBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.imageWebsite.setImageResource(item.third)
//        holder.imageWebsite.setImageResource(R.mipmap.ic_launcher_round)
        holder.textTitle.text = item.first
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: ItemRecommendBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        val imageWebsite = binding.imageRecommend
        val textTitle = binding.textRecommend

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            listener.onRecommendItemClick(values[adapterPosition].second)
        }
    }

    interface OnRecommendClickListener {
        fun onRecommendItemClick(url: String)
    }
}