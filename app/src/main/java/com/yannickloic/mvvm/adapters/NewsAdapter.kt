package com.yannickloic.mvvm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yannickloic.mvvm.R
import com.yannickloic.mvvm.models.Article
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
    }

    /*
    ici on n'utilise pas list.notifyDatasetChanged
    car en l'utilisant, l'adaptateur recyclerview mettra toujours à jour tous les éléments même s'ils ne sont pas modifiés

    pour résoudre ce problème, nous utilisons DiffUtil
    il calcule la différence entre deux listes et nous permet de mettre à jour uniquement les éléments qui sont différents
    fonctionne également en arrière-plan afin de ne pas bloquer le fil principal
     */
    private val differCallback= object : DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    //outil qui prendra les deux listes et dira les différences
    val differ= AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_article_preview, parent, false))
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article= differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(ivArticleImage)
            tvSource.text= article.source?.name
            tvTitle.text= article.title
            tvDescription.text= article.description
            tvPublishedAt.text= article.publishedAt
            setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    private var onItemClickListener:((Article)->Unit)?=null

    fun setOnItemClickListener(listener: (Article)->Unit){
        onItemClickListener= listener
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }
}