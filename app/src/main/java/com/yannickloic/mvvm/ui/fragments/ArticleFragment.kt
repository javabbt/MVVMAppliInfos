package com.yannickloic.mvvm.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.yannickloic.mvvm.R
import com.yannickloic.mvvm.ui.MainActivity
import com.yannickloic.mvvm.ui.NewsViewModel
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : Fragment(R.layout.fragment_article){
    lateinit var viewModel: NewsViewModel
    //obtenir des articles comme élément global
    private val args: ArticleFragmentArgs by navArgs() //ArticleFragmentArgs est généré par le composant de navigation lorsque nous ajoutons les arguments

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= (activity as MainActivity).viewModel

        //obtenir l'article actuel qui a été passé en tant qu'arguments à ce fragment
        val article = args.article
        webView.apply {
            webViewClient= WebViewClient()
            loadUrl(article.url.toString())
        }

        //sauvegarder article
        fab.setOnClickListener{
            viewModel.saveArticle(article)
            Snackbar.make(view, " Article Sauvegarde avec succes! ", Snackbar.LENGTH_SHORT).show()
        }


    }
}