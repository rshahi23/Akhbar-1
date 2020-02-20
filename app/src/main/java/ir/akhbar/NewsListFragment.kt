package ir.akhbar

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_news_list.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsListFragment : Fragment() {

    private lateinit var handler: Handler
    private lateinit var searchRunnable: Runnable

    private lateinit var networking: Networking

    private var searchQuery = "Iran"

    private val defaultQuery = "Iran"

    private lateinit var databaseThread: DatabaseThread

    private lateinit var newsDao: NewsDao

    private lateinit var updateDatabaseTask: UpdateDatabaseTask

    private lateinit var queryDatabaseTask: QueryDatabaseTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val applicationDatabase = Room.databaseBuilder(
                requireContext(),
                ApplicationDatabase::class.java,
                "application_database"
        ).build()
        newsDao = applicationDatabase.newsDao
        databaseThread = DatabaseThread()
        networking = Networking()
        handler = Handler()
        searchRunnable = Runnable {
            searchProgress.visibility = View.VISIBLE
            actionSearch.visibility = View.GONE
            fetchData(networking, searchQuery)
        }

        updateDatabaseTask = UpdateDatabaseTask(newsDao)
        queryDatabaseTask = QueryDatabaseTask(object : QueryDatabaseCallback {
            override fun onQuerySuccess(newsDatas: Array<NewsData>) {
                updateRecyclerView(newsDatas)
            }

            override fun onQueryFailure() {
                failureView.visibility = View.VISIBLE
            }
        }, newsDao)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionSearch.setOnClickListener {
            toolbarTitle.visibility = View.GONE
            searchInput.visibility = View.VISIBLE
        }

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable) {
                searchQuery = if (s.toString().isEmpty()) {
                    defaultQuery
                } else {
                    s.toString()
                }
                handler.removeCallbacks(searchRunnable)
                handler.postDelayed(searchRunnable, 1000)
            }
        })

        newsRecycler.layoutManager = LinearLayoutManager(context)
        retryButton.setOnClickListener {
            failureView.visibility = View.GONE
            progress.visibility = View.VISIBLE
            fetchData(networking, defaultQuery)
        }

        fetchData(networking, defaultQuery)
    }

    private fun fetchData(networking: Networking, query: String) {
        networking.server
                .getNewsList(query, "6fba2629782d465abd2dc5f427223cc0")
                .enqueue(object : Callback<ServerResponse> {
                    override fun onResponse(call: Call<ServerResponse>, response: Response<ServerResponse>) {
                        updateRecyclerView(response.body()!!.articles)
                    }

                    override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                        progress!!.visibility = View.GONE
                        queryDatabaseTask.execute()
                    }
                })
    }

    private fun updateRecyclerView(newsData: Array<NewsData>) {
        progress.visibility = View.GONE
        searchProgress.visibility = View.GONE
        actionSearch.visibility = View.VISIBLE
        updateDatabase(newsData)
        val adapter = NewsAdapter(newsData, NewsItemClickListenerKt { data ->
            val bundle = Bundle()
            bundle.putString("newsTitle", data.newsTitle)
            bundle.putString("newsDescription", data.newsDescription)
            bundle.putString("newsImage", data.newsImage)
            bundle.putString("newsUrl", data.url)
            val detailFragment = NewsDetailFragment()
            detailFragment.arguments = bundle
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragmentContainer, detailFragment)
                    .commit()
        })
        newsRecycler.adapter = adapter
    }

    private fun updateDatabase(newsData: Array<NewsData>) {
        updateDatabaseTask.execute(*newsData)
    }

    private fun mapNewsTableToNewsData(newsTable: NewsTable): NewsData {
        return NewsData(newsTable.title, newsTable.description, newsTable.urlToImage, newsTable.url)
    }

    fun canHandleBackPressed(): Boolean {
        var canHandleBackPressed = false
        if (searchInput.visibility == View.VISIBLE) {
            searchInput.visibility = View.GONE
            toolbarTitle.visibility = View.VISIBLE
            searchProgress.visibility = View.VISIBLE
            actionSearch.visibility = View.GONE
            fetchData(networking, defaultQuery)
            canHandleBackPressed = true
        }
        return canHandleBackPressed
    }

    override fun onDestroyView() {
        if (searchInput.visibility == View.VISIBLE) {
            searchInput.visibility = View.GONE
            toolbarTitle.visibility = View.VISIBLE
        }
        super.onDestroyView()
    }
}
