package com.yannickloic.mvvm.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yannickloic.mvvm.NewsApplication
import com.yannickloic.mvvm.R
import com.yannickloic.mvvm.models.Article
import com.yannickloic.mvvm.models.NewsResponse
import com.yannickloic.mvvm.repository.NewsRepository
import com.yannickloic.mvvm.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    app: Application,
    val newsRepository: NewsRepository //parametre
) : AndroidViewModel(app){ //hériter du ViewModel Android pour utiliser le contexte de l'application
    //ici, nous utilisons le contexte de l'application pour obtenir le contexte tout au long de l'exécution de l'application,
    //donc cela fonctionnera même si l'activité change ou se détruit, le contexte de l'application fonctionnera toujours jusqu'à ce que l'application s'exécute

    //LIVEDATA
    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    //Pagination
    var breakingNewsPage= 1
    var searchNewsPage= 1
    var breakingNewsResponse : NewsResponse? = null
    var searchNewsResponse : NewsResponse? = null


    init {
        getBreakingNews("fr")
    }

    //we cannot start the function in the coroutine so we start the it here
    /*
    viewModelScope makes the function alive only as long as the ViewModel is alive
     */
    fun getBreakingNews(countryCode: String)= viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }


    fun searchNews(searchQuery: String)= viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }


    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse== null){
                    breakingNewsResponse= resultResponse //si la première page enregistre le résultat dans la réponse
                }else{
                    val oldArticles= breakingNewsResponse?.articles //sinon, ajoutez tous les articles à l'ancien
                    val newArticle= resultResponse.articles //ajouter une nouvelle réponse au nouveau
                    oldArticles?.addAll(newArticle) //ajouter de nouveaux articles aux anciens articles
                }
                return  Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>{
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsResponse== null){
                    searchNewsResponse= resultResponse //si la première page enregistre le résultat dans la réponse
                }else{
                    val oldArticles= searchNewsResponse?.articles //sinon, ajoutez tous les articles à l'ancien
                    val newArticle= resultResponse.articles //ajouter une nouvelle réponse au nouveau
                    oldArticles?.addAll(newArticle) //ajouter de nouveaux articles aux anciens articles
                }
                return  Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    /*
    fonction pour enregistrer les articles dans la base de données: coroutine
     */
    fun saveArticle(article: Article)= viewModelScope.launch {
        newsRepository.upsert(article)
    }

    /*
    fonction pour obtenir tous les articles de presse enregistrés
     */
    fun getSavedArticle()= newsRepository.getSavedNews()

    /*
    fonction pour supprimer l'article de la base de données
     */
    fun deleteSavedArticle(article: Article)= viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private suspend fun safeBreakingNewsCall(countryCode: String){
        breakingNews.postValue(Resource.Loading())
        try{
            if (hasInternetConnection()){
                val response= newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                //traitement reponse
                breakingNews.postValue(handleBreakingNewsResponse(response))
            }else{
                breakingNews.postValue(Resource.Error("Pas de connexion Internet"))
            }

        } catch (t: Throwable){
            when(t){
                is IOException-> breakingNews.postValue(Resource.Error("Panne de réseau"))
                else-> breakingNews.postValue(Resource.Error("Erreur de Conversion"))
            }
        }
    }

    private suspend fun safeSearchNewsCall(searchQuery: String){
        searchNews.postValue(Resource.Loading())
        try{
            if (hasInternetConnection()){
                val response= newsRepository.searchNews(searchQuery, searchNewsPage)
                //traitement reponse
                searchNews.postValue(handleSearchNewsResponse(response))
            }else{
                searchNews.postValue(Resource.Error("Pas de connexion Internet"))
            }

        } catch (t: Throwable){
            when(t){
                is IOException-> searchNews.postValue(Resource.Error("Panne de réseau"))
                else-> searchNews.postValue(Resource.Error("Erreur de Conversion"))
            }
        }
    }


    private fun hasInternetConnection(): Boolean{
        val connectivityManager= getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork= connectivityManager.activeNetwork?: return false
        val capabilities= connectivityManager.getNetworkCapabilities(activeNetwork)?: return false

        return when{
            capabilities.hasTransport(TRANSPORT_WIFI)-> true
            capabilities.hasTransport(TRANSPORT_CELLULAR)-> true
            capabilities.hasTransport(TRANSPORT_ETHERNET)->true
            else -> false
        }
    }
}