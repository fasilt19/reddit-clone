package com.kolo.redditclone.activities

import android.os.Bundle
import android.view.View.INVISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.VolleyError
import com.kolo.redditclone.adapters.PostAdapter
import com.kolo.redditclone.databinding.ActivitySearchBinding
import com.kolo.redditclone.interfaces.NetworkResponseInterface
import com.kolo.redditclone.models.MainModel
import com.kolo.redditclone.utils.AppConstants.BASE_URL
import com.kolo.redditclone.utils.AppUtils.newRequest
import org.json.JSONArray
import org.json.JSONObject

class SearchActivity : AppCompatActivity(), NetworkResponseInterface {
    private lateinit var binding: ActivitySearchBinding
    private var mainModel: ArrayList<MainModel> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvPost.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = PostAdapter(this@SearchActivity, mainModel)
        }

        search(intent.getStringExtra("query").toString())

        binding.imgBack.setOnClickListener { onBackPressed() }
    }

    private fun search(query: String) {
        binding.txtSearch.text = query
        newRequest(BASE_URL + "search.json?q=" + query, this)
    }

    override fun onResponse(response: String) {
        binding.progressCircular.visibility = INVISIBLE
        val jsonObject = JSONObject(JSONObject(response).getString("data"))
        val data = JSONArray(jsonObject.getString("children"))

        for (i in 0 until data.length()) {
            val obj = JSONObject(data.getJSONObject(i).getString("data"))
            mainModel.add(
                MainModel(
                    "" + obj.getString("subreddit_id"),
                    "" + obj.getString("subreddit_name_prefixed"),
                    "" + obj.getString("author"),
                    "" + obj.getString("thumbnail"),
                    if (obj.getBoolean("is_self"))
                        "" + obj.getString("selftext")
                    else
                        "" + obj.getString("title"),
                    "" + obj.getString("ups"),
                    "" + obj.getString("downs"),
                    "" + obj.getString("num_comments"),
                    obj.getInt("created_utc").toString().toLong(),
                    if (obj.getBoolean("is_self"))
                        0
                    else
                        1
                )
            )
        }
        binding.rvPost.adapter!!.notifyDataSetChanged()
    }

    override fun onError(error: VolleyError) {
    }
}