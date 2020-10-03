package com.example.beelinetesttask

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.beelinetesttask.models.Article
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_news_detail.*

class NewsDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
        setSupportActionBar(toolbar_detail)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }



        val article = intent.getParcelableExtra<Article>("ArticleDetails")
        article?.let { populateData(it) }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun populateData(article: Article) {
        Picasso.get()
            .load(article.urlToImage)
            .placeholder(R.drawable.placeholder_img)
            .resize(1000, 500)
            .centerCrop()
            .into(Img)
        article_title.text = "ЗАГОЛОВОК:${article.title}"
        author.text = "АВТОР:${article.author}"
        description.text = "ОПИСАНИЕ:${article.description}"
        url.text = "ССЫЛКА:${article.url}"
        publishedAt.text = "ОПУБЛИКОВАНО:${article.publishedAt}"
        content.text = "КОНТЕНТ:${article.content}"
    }
}