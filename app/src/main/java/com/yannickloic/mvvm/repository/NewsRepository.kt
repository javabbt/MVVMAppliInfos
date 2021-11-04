package com.yannickloic.mvvm.repository

import com.yannickloic.mvvm.api.RetrofitInstance
import com.yannickloic.mvvm.db.ArticleDatabase
import com.yannickloic.mvvm.models.Article

/*
obtenir des données de la base de données et de la source de données distante (retrofit api)
 */
class NewsRepository(
    val db: ArticleDatabase //parametre
) {

    /*
    fonction qui interroge directement notre API pour les dernières nouvelles
     */
    suspend fun getBreakingNews(countryCode:String, pageNumber:Int)=
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    /*
    fonction qui interroge notre API pour les actualités recherchées
     */
    suspend fun searchNews(searchQuery: String, pageNumber: Int)=
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    /*
    fonction pour insérer un article dans la base de données
     */
    suspend fun upsert(article: Article)=
        db.getArticleDao().upsert(article)

    /*
    fonction pour obtenir des nouvelles enregistrées de db
     */
    fun getSavedNews()=
        db.getArticleDao().getAllArticles()

    /*
    fonction pour supprimer des articles de la base de données
     */
    suspend fun deleteArticle(article: Article)=
        db.getArticleDao().deleteArticle(article)
}