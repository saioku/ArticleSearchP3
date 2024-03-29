package com.codepath.articlesearch

/**
 * This interface is used by the [BestSellerBooksRecyclerViewAdapter] to ensure
 * it has an appropriate Listener.
 *
 * In this app, it's implemented by [BestSellerBooks]
 */
interface OnListFragmentInteractionListener {
    fun onItemClick(item: BestSellerBook)
}