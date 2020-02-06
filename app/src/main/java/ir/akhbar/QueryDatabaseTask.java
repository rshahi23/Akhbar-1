package ir.akhbar;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class QueryDatabaseTask extends AsyncTask<Void, Void, NewsData[]> {

    private NewsDao newsDao;

    private QueryDatabaseCallback callback;

    public QueryDatabaseTask(QueryDatabaseCallback callback, NewsDao dao) {
        this.newsDao = dao;
        this.callback = callback;
    }

    @Override
    protected NewsData[] doInBackground(Void... noData) {
        final List<NewsTable> news = new ArrayList<>();
        news.addAll(newsDao.getAllNews());
        NewsData[] newsDatas = new NewsData[news.size()];
        if (!news.isEmpty()) {
            for (int i = 0; i < news.size(); i++) {
                NewsTable newsTable = news.get(i);
                NewsData newsData = mapNewsTableToNewsData(newsTable);
                newsDatas[i] = newsData;
            }
            return newsDatas;
        } else {
            return new NewsData[0];
        }
    }

    @Override
    protected void onPostExecute(NewsData[] newsDatas) {
        super.onPostExecute(newsDatas);
        if (newsDatas.length > 0) {
            callback.onQuerySuccess(newsDatas);
        } else {
            callback.onQueryFailure();
        }
    }

    private NewsData mapNewsTableToNewsData(NewsTable newsTable) {
        return new NewsData(newsTable.getTitle(), newsTable.getDescription(), newsTable.getUrlToImage(), newsTable.getUrl());
    }
}
