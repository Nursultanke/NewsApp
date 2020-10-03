package com.example.beelinetesttask.services

import com.example.beelinetesttask.models.Article
import com.example.beelinetesttask.models.News
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface NewsService {

    @GET(value = "v2/everything/")
    suspend fun getNews(@QueryMap queryMap: MutableMap<String, String>): Response<News>
}