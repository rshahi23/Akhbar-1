package ir.akhbar;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class UpdateDatabaseTask extends AsyncTask<NewsData, String, Boolean> {

    private NewsDao newsDao;

    public UpdateDatabaseTask(NewsDao dao) {
        this.newsDao = dao;
    }

    @Override
    protected Boolean doInBackground(NewsData... newsDatas) {
        List<NewsTable> news = new ArrayList<>();
        for (NewsData newsData : newsDatas) {
            NewsTable newsTable = mapNewsDataToNewsTable(newsData);
            news.add(newsTable);
        }
        List<NewsTable> newsTables = newsDao.getAllNews();
        if (!newsTables.isEmpty()) {
            newsDao.deleteAllNews(newsTables);
        }
        newsDao.addNewsList(news);

        return true;
    }

    private NewsTable mapNewsDataToNewsTable(NewsData newsData) {
        return new NewsTable(newsData.getNewsTitle(), newsData.getNewsDescription(), newsData.getNewsImage(), newsData.getUrl());
    }

}
