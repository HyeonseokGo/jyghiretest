package com.example.jyghiretest.product.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.jyghiretest.model.Category
import com.example.jyghiretest.product.list.ProductsByCategoryFragment

class CategoriesAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var categories: List<Category> = listOf()

    override fun getItemCount() = categories.size


    override fun createFragment(position: Int): Fragment {
        return ProductsByCategoryFragment.newInstance(
            categories[position].key
        )
    }

    fun updateCategories(categories: List<Category>) {
        if (this.categories == categories) {
            return
        }

        this.categories = categories
        notifyDataSetChanged()
    }

    fun getTitleByPosition(position: Int): String {
        return categories[position].name
    }

}