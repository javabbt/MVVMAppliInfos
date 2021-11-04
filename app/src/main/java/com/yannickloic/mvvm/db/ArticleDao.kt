package com.yannickloic.mvvm.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yannickloic.mvvm.models.Article

/*
Data Access Object
Ceci est une interface pour accéder à la base de données
Ici, nous définissons des fonctions pour accéder à la base de données locale
enregistrer des articles, lire des articles, supprimer des articles
 */
@Dao //annoter pour faire savoir qu'il s'agit de l'interface qui définit la fonction

interface ArticleDao {

    /*
    fonction pour insérer ou mettre à jour un article
    conflictStrategy est pour définir ce qu'il faut faire, si un conflit se produit dans la base de données, comme il existe déjà dans la base de données
    ici nous le REMPLACONS
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long // function(parameter):return --> here we return ID


    /*
    query function pour retourner tous les articles disponibles dans notre base de données
    LiveData: classe de composants d'architecture Android qui permet aux fragments de s'abonner aux modifications de données en direct
    lorsque les données changent, liveData notifiera tous les fragments afin qu'ils puissent mettre à jour les vues
    utile en rotation
     */
    @Query("SELECT* FROM articles")
    fun getAllArticles():LiveData<List<Article>>


    /*
    Fonction de suppression
     */
    @Delete
    suspend fun deleteArticle(article: Article)
}