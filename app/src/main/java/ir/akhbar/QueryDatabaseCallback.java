package ir.akhbar;

public interface QueryDatabaseCallback {

    void onQuerySuccess(NewsData[] newsDatas);

    void onQueryFailure();
}
