package com.yannickloic.mvvm.api

import com.yannickloic.mvvm.models.NewsResponse
import com.yannickloic.mvvm.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    /* ici, nous définissons une requête unique que nous pouvons exécuter à partir du code
     */
    //nous utilisons l'interface Api pour accéder à l'API pour la demande
    //nous devons spécifier le type de requête http -GET ici
    //et nous retournons les réponses de l'API

    @GET("v2/top-headlines")

    //fonction
    //async
    //coroutine
    suspend fun getBreakingNews(
        //parametres de la fonction
    @Query("country")
    countryCode: String = "fr", //default a us

    @Query("page")  //paginer la requete
    pageNumber: Int= 1,

    @Query("apiKey")
    apiKey: String= API_KEY

    ):Response<NewsResponse> //return response


    @GET("v2/everything")

    //fonction
    //async
    //coroutine
    suspend fun searchForNews(
        //parametres de la fonction
        @Query("q")
        searchQuery: String,
        @Query("page")  //paginer la requete
        pageNumber: Int= 1,
        @Query("apiKey")
        apiKey: String= API_KEY
    ):Response<NewsResponse> //return response
}