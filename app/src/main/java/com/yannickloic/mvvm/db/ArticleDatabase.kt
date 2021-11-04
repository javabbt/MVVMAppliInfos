package com.yannickloic.mvvm.db

import android.content.Context
import androidx.room.*
import com.yannickloic.mvvm.models.Article

/*
la classe Database de Room doit toujours etre abstraite
@Database annotation pour que room reconnaisse la classe
pass parameter:
    Liste d'entities, ici on a une seule table: article

 */
@Database(
    entities = [Article::class],
    version = 1
)

@TypeConverters(Converters::class)

abstract class ArticleDatabase : RoomDatabase(){

    //fuoction qui retourne ArticleDao
    abstract fun getArticleDao(): ArticleDao

    //companion object pour creer une database
    companion object{
        //Volatile afin que les autres threads puissent voir immédiatement quand le thread modifie cette instance
        @Volatile
        private var instance: ArticleDatabase? =null
        //Verrouiller la variable pour synchroniser l'instance, pour s'assurer qu'il n'y a qu'une seule instance de ArticleDatabase à la fois
        private val LOCK= Any()

        //fonction d'opérateur appelée chaque fois que nous créons l'instance de notre base de données
        //si l'instance est nulle, nous la synchronisons avec LOCK :
        //LOCK : pour que tout ce qui se passe à l'intérieur de cette fonction ne soit pas accessible par d'autres threads en même temps

        //sync instance seulement si null
        operator fun invoke(context: Context) = instance?: synchronized(LOCK){

            // null check "pour s'assurer qu'il n'y a pas d'autre thread qui définit l'instance sur quelque chose alors que nous avons déjà défini

            instance ?: createDatabase(context).also{ instance = it }

        }

        private fun createDatabase(context: Context)=
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()

    }
}