package ir.akhbar

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NewsDaoKt {

    @Query("SELECT * FROM news_table")
    fun getAllNews(): List<NewsTable>

    @Insert(entity = NewsTable::class)
    fun addNewsList(news: List<NewsTable>)

    @Delete(entity = NewsTable::class)
    fun deleteAllNews(news: List<NewsTable>)

}