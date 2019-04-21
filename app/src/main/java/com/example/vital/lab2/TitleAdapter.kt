package com.example.vital.lab2

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.title_list_item.view.*

class TitleAdapter(val items: ArrayList<Movie>, val context: Context): RecyclerView.Adapter<TitleAdapter.ViewHolder>() {
    var onItemClick: ((String) -> Unit)? = null

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.movieTitle?.text = items.get(position).Title
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.title_list_item, parent, false));
    }


    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val movieTitle = view.movie_title

        init {
            view.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition].Title)
            }
        }
    }
}