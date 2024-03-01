package com.codepath.articlesearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.codepath.articlesearch.R.layout
import okhttp3.Headers
import org.json.JSONObject

private const val API_KEY = BuildConfig.API_KEY

class BestSellerBooks : Fragment(), OnListFragmentInteractionListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layout.best_seller_list, container, false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val context = view.context
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        updateAdapter(progressBar, recyclerView)
        return view
    }


    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView) {
        progressBar.show()

        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api-key"] = API_KEY

        client[
                "https://api.nytimes.com/svc/books/v3/lists/current/hardcover-fiction.json",
                params,
                object : JsonHttpResponseHandler()
                {

                    override fun onSuccess(
                        statusCode: Int,
                        headers: Headers,
                        json: JSON
                    ) {
                        progressBar.hide()

                        val resultsJSON : JSONObject = json.jsonObject.get("results") as JSONObject
                        val booksRawJSON : String = resultsJSON.get("books").toString()
                        val gson = Gson()
                        val arrayTutorialType = object : TypeToken<List<BestSellerBook>>() {}.type
                        val models : List<BestSellerBook> = gson.fromJson(booksRawJSON, arrayTutorialType)
                        recyclerView.adapter = BestSellerBooksRecyclerViewAdapter(models, this@BestSellerBooks)

                        Log.d("BestSellerBooksFragment", "response successful")
                    }


                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        errorResponse: String,
                        t: Throwable?
                    ) {
                        progressBar.hide()

                        t?.message?.let {
                            Log.e("BestSellerBooksFragment", errorResponse)
                        }
                    }
                }]
    }


    override fun onItemClick(item: BestSellerBook) {
        Toast.makeText(context, "test: " + item.title, Toast.LENGTH_LONG).show()
    }

}
