package com.kolo.redditclone.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kolo.redditclone.models.SearchModel

@Dao
interface SearchDao {
    @Insert
    fun insert(searchModel: SearchModel)

    @Query("select * from search_table where `query` like :query")
    fun isPresent(query: String): List<SearchModel>

    @Query("select EXISTS(select * from search_table where `query`= :value)")
    fun isExist(value:String): Boolean

    @Query("select * from search_table")
    fun getAll():List<SearchModel>
}