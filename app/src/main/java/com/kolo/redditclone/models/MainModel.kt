package com.kolo.redditclone.models

data class MainModel(
    val id: String,
    val name: String,
    val author: String,
    val thumbnail: String,
    val title: String,
    val likes: String,
    val dislikes: String,
    val comments: String,
    val timestamp: Long,
    val type: Int
)
