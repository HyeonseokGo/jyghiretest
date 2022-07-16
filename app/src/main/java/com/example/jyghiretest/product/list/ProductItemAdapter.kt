package com.example.jyghiretest.product.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.jyghiretest.R
import com.example.jyghiretest.databinding.ItemProductBinding
import com.example.jyghiretest.model.Product

class ProductItemAdapter(
    private val onItemClick: (key: String) -> Unit,
    private val onFavoriteClick: (key: String) -> Unit,
) : ListAdapter<Product, ProductsByCategoryViewHolder>(ProductItemDiffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ProductsByCategoryViewHolder(
        onItemClick = onItemClick,
        onFavoriteClick = onFavoriteClick,
        binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ProductsByCategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class ProductsByCategoryViewHolder(
    private val onItemClick: (key: String) -> Unit,
    private val onFavoriteClick: (key: String) -> Unit,
    private val binding: ItemProductBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Product) {
        binding.holderLayout.setOnClickListener {
            onItemClick(item.key)
        }

        binding.textViewProductName.text = item.name

        val drawableRes =
            if (item.isFavorite) R.drawable.ic_baseline_favorite_checked_24 else R.drawable.ic_baseline_favorite_unchecked_24
        val drawable = ContextCompat.getDrawable(binding.root.context, drawableRes)
        binding.imageButtonFavorite.setImageDrawable(drawable)
        binding.imageButtonFavorite.setOnClickListener {
            onFavoriteClick(item.key)
        }
    }

}

object ProductItemDiffUtil : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.key == newItem.key
    }

}
