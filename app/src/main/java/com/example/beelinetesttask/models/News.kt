package com.example.beelinetesttask.models

import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("status")
    val status: String,
    val totalResults: Int,
    val articles: ArrayList<Article>
)