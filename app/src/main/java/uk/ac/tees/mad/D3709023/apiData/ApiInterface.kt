package uk.ac.tees.mad.D3709023.apiData

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {
    @Headers(
        "X-RapidAPI-Key: 4c12cb5211mshe3eeb4af97a828cp17a2e5jsn71ac77dc5a20",
        "X-RapidAPI-Host: deezerdevs-deezer.p.rapidapi.com"
    )

    @GET("/search")
     suspend fun getData(@Query("q") query: String): Response<MyData>
//     suspend fun getMusicData():Album
}