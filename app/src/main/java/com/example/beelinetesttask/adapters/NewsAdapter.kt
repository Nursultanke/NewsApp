package com.example.beelinetesttask.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beelinetesttask.R
import com.example.beelinetesttask.models.Article
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.news_item.view.*


class NewsAdapter :
    RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {
    private var mArticleList: List<Article>? = null
    private var onClickListener: OnArticleClickListener? = null

    fun setArticles(articleList: List<Article>) {
        if (mArticleList != null) mArticleList = null
        mArticleList = articleList
        notifyDataSetChanged()
    }

    fun setOnClickListener(listener: OnArticleClickListener) {
        this.onClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int = mArticleList?.size ?: 0


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        mArticleList?.let { holder.bind(mArticleList!![position]) }
        onClickListener?.let { holder.initialize(it) }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val NewsImg: ImageView = itemView.NewsImg
        private val NewsTitle: TextView = itemView.NewsTitle
        private val NewsDesc: TextView = itemView.NewsDesc

        fun bind(item: Article) {
            Picasso.get()
                .load(item.urlToImage)
                .placeholder(R.drawable.img)
                .resize(1000, 1000)
                .centerCrop()
                .into(NewsImg)
            NewsTitle.text = item.title
            NewsDesc.text = item.description
            itemView.setOnClickListener {

            }
        }
        fun initialize(action: OnArticleClickListener) {
            itemView.setOnClickListener {
                action.onArticleClick(adapterPosition)
            }
        }


    }

    interface OnArticleClickListener {
        fun onArticleClick(position: Int)
    }
}