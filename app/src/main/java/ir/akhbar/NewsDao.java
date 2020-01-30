package ir.akhbar;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsDao {

    @Query("SELECT * FROM news_table")
    List<NewsTable> getAllNews();

    @Insert(entity = NewsTable.class)
    void addNewsList(List<NewsTable> news);
}
