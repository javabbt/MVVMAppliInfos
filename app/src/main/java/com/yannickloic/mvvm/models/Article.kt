package com.yannickloic.mvvm.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/*
Pour enregistrer l'article dans la base de données, nous devons annoter la classe avec entity
qui indiquera au studio Android que cette classe d'article est la table de la base de données

Ici l'article sera la table entière et avec des colonnes
 */
@Entity(
    tableName = "articles"
)
data class Article(
    @PrimaryKey(autoGenerate = true) //ajouter id comme cle primaire
    var id: Int?= null, //tous les articles n'auront pas d'identifiant, donc le définir sur null
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
) : Serializable
//serializer pour envoyer toute la classe avec un Bundle