package com.example.jyghiretest.home;

import android.content.Context
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment
import com.example.jyghiretest.R
import com.example.jyghiretest.favorite.FavoriteProductsFragment
import com.example.jyghiretest.product.home.ProductHomeFragment

enum class HomeViewPagerFragments(@StringRes val titleRes: Int, val factory: () -> Fragment) {
    PRODUCT_HOME(R.string.label_product_home, {
        ProductHomeFragment.newInstance()
    }),
    FAVORITE_PRODUCTS(R.string.label_favorite_products, {
        FavoriteProductsFragment.newInstance()
    });

    fun requireTitle(context: Context): String {
        return context.getString(titleRes)
    }

    companion object {
        fun getByPosition(position: Int) = values()[position]
    }

}
