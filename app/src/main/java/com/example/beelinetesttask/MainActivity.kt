package com.example.beelinetesttask

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beelinetesttask.adapters.NewsAdapter
import com.example.beelinetesttask.models.Article
import com.example.beelinetesttask.models.News
import com.example.beelinetesttask.services.NewsService
import com.example.beelinetesttask.services.RetrofitInstance
import com.example.beelinetesttask.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response

class MainActivity : AppCompatActivity(), NewsAdapter.OnArticleClickListener {

    var page = 1
    var isLoading = false
    val limit = 10
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var mAdapter: NewsAdapter
    var visibileItemCount: Int = 0
    var totalItemCount: Int = 0
    var pastVisibleItemCount: Int = 0
    private var mArticleList: MutableList<Article>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        layoutManager = LinearLayoutManager(this)
        recyclerview.layoutManager = layoutManager

        recyclerview.setHasFixedSize(true)
        initRecyclerView()
        initAdapter()
        swipeRefresh.setOnRefreshListener {
            initAdapter()
            swipeRefresh.isRefreshing = false
        }

    }


    private fun initRecyclerView() {

        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                if (dy >= 0) {
                    visibileItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisibleItemCount =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (isLoading) {
                        if ((visibileItemCount + pastVisibleItemCount) >= totalItemCount) {
                            page + 1
                            initAdapter()
                        }
                    }
//                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun initAdapter() {
        isLoading = true
        progressbar.visibility = View.VISIBLE
        val start = (page - 1) * limit
        val end = (page) * limit
        mAdapter = NewsAdapter()
        getNews().observe(this, Observer {
            val articleList = it.body()?.articles
            if (articleList != null) {
                for (i in start..end) {
                    mArticleList?.add(articleList[i])
                    Log.e("Tag", it.body().toString())
                    mArticleList?.let { it1 -> mAdapter.setArticles(it1) }
                }
//                mAdapter.notifyDataSetChanged()

            }
        })
        recyclerview.adapter = mAdapter
        if (::mAdapter.isInitialized){
            mAdapter.notifyDataSetChanged()
            progressbar.visibility = View.GONE
            isLoading = false
        }
        else{
            recyclerview.adapter = mAdapter
        }
        mAdapter.setOnClickListener(this)
    }



    private fun getNews(): LiveData<Response<News>> {
        val retService = RetrofitInstance
            .getRetrofitInstance()
            .create(NewsService::class.java)

        val queryMap = mutableMapOf<String, String>()
        queryMap["q"] = "science"
        queryMap["apiKey"] = Constants.apiKey
        queryMap["sortBy"] = Constants.sortBy[0]
        queryMap["from"] = Constants.from
        queryMap["to"] = Constants.to
        queryMap["language"] = Constants.lang
        return liveData {
            val response: Response<News> = retService.getNews(queryMap = queryMap)
            Log.e("TAG", "onCreate: $queryMap")
            emit(response)
        }
    }

    override fun onArticleClick(position: Int) {
        val i = Intent(this, NewsDetail::class.java)
        i.putExtra("ArticleDetails", mArticleList?.get(position))
        startActivity(i)

    }
}