package ir.akhbar

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkingKt {

    private val server: NewsListServer

    init {
        val client2 = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        val retrofit = Retrofit.Builder()
                .client(client2)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://newsapi.org/")
                .build()

        server = retrofit.create(NewsListServer::class.java)
    }

    fun getServer(): NewsListServer {
        return server
    }

}