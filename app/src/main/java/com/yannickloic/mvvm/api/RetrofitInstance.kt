package com.yannickloic.mvvm.api

import com.yannickloic.mvvm.utils.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
    cette classe Retrofit Instance nous permet de créer des requêtes depuis n'importe où dans notre code

 */
class RetrofitInstance {
    companion object{
        private val  retrofit by lazy {
            //lazy signifie que nous ne l'initialisons ici qu'une seule fois

            val logging= HttpLoggingInterceptor()
            /* ce HTTP LOGGING INTERCEPTOR dependency est capable d'enregistrer les réponses de modernisation
            cela sera utile pour déboguer le code
             */
            //attacher a l'object Retrofit
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)  //voir le corps de la reponse
            //network client
            val client= OkHttpClient.Builder().addInterceptor(logging).build()

            //passer le client au retrofit instance
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
        //addConverterFactory est utilisé pour déterminer comment la réponse doit être interprétée et convertie en objet kotlin
                .client(client)
                .build()


        }

        //get api instance from retrofit builder
        //api object
        // this can be used from everywhere to make network request
        val api by lazy {
            retrofit.create(NewsApi::class.java)
        }
    }
}