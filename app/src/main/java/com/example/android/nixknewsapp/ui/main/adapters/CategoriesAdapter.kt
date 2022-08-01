package com.example.android.nixknewsapp.ui.main.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.nixknewsapp.R
import com.example.android.nixknewsapp.data.model.Category
import com.example.android.nixknewsapp.databinding.ItemsCategoriesBinding

class CategoriesAdapter(
    var categories: List<Category>,
    private val onItemClicked: (Category) -> Unit,
): RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemsCategoriesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

//        init {
//            binding.imageView.setOnClickListener {
//                val popupMenu = PopupMenu(binding.imageView.context, it)
//                popupMenu.inflate(R.menu.popup_menu)
//                popupMenu.setOnMenuItemClickListener { menu ->
//                    when (menu.itemId) {
//                        R.id.menu_save -> {
//
//                        }
//                    }
//                }
//            }
//        }
        fun bind(category: Category, context: Context) {
            Glide.with(context).load(category.imageId).into(binding.imageView)
            binding.tvTitle.text = category.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemsCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCategory = categories[position]
        holder.itemView.setOnClickListener {
            onItemClicked(currentCategory)
        }
        holder.bind(currentCategory, holder.itemView.context)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

}