package ir.akhbar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class NewsAdapterKt(
        private val newsArray: List<NewsData>,
        private val itemClickListener: NewsItemClickListener
) : RecyclerView.Adapter<NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_new_list, parent, false)
        return NewsViewHolder(itemView, itemClickListener)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsArray[position])
    }

    override fun getItemCount(): Int {
        return newsArray.size
    }

}