package com.kolo.redditclone.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kolo.redditclone.interfaces.SearchDao
import com.kolo.redditclone.models.SearchModel

@Database(entities = [SearchModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun SearchDao(): SearchDao
}