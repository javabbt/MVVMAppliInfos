package com.yannickloic.mvvm.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yannickloic.mvvm.repository.NewsRepository
/*
Pour définir comment notre modèle de vue doit être créé
 */
class NewsViewModelProviderFactory(
    val app: Application,
    val newsRepository: NewsRepository
    ) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(app, newsRepository) as T
    }
}