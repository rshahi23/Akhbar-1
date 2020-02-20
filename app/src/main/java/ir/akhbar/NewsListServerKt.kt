package ir.akhbar

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsListServerKt {

    @GET("v2/everything")
    fun getNewsList(
            @Query("q") q: String,
            @Query("apiKey") apiKey: String
    ): Call<ServerResponse>
}