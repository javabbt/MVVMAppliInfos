package com.yannickloic.mvvm.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.yannickloic.mvvm.R
import com.yannickloic.mvvm.adapters.NewsAdapter
import com.yannickloic.mvvm.ui.MainActivity
import com.yannickloic.mvvm.ui.NewsViewModel
import kotlinx.android.synthetic.main.fragment_saved_news.*

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //définir viewModels pour fragmentActivity
        //et on peut caster cela en MainActivity afin que nous puissions avoir accès au modèle de vue créé à MainActivity
        viewModel= (activity as MainActivity).viewModel

        setupRecyclerView()


        newsAdapter.setOnItemClickListener {
            val bundle= Bundle().apply{
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment,
                bundle
            )
        }

        //glisser supprimer la variable
        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position= viewHolder.adapterPosition
                val article= newsAdapter.differ.currentList[position]
                viewModel.deleteSavedArticle(article)
                //exécuter annuler Snackbar
                Snackbar.make(view, " Article supprimé avec succès ", Snackbar.LENGTH_LONG).apply {
                    setAction("Annuler") {
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }
        //item touch helper
        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(rvSavedNews)
        }

        //obtenir des nouvelles enregistrées, observer les changements sur notre base de données
        viewModel.getSavedArticle().observe(viewLifecycleOwner, Observer { articles -> //nouvelle liste d'articles
            newsAdapter.differ.submitList(articles) //update recyclerview //differ calculera la différence entre les listes
        })
    }

    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}