package ir.akhbar

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_table")
data class NewsTableKt(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val title: String,
        val description: String,
        val urlToImage: String,
        val url: String
)