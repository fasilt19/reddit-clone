package com.kolo.redditclone.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.VolleyError
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kolo.redditclone.R
import com.kolo.redditclone.adapters.PostAdapter
import com.kolo.redditclone.databinding.FragmentPopularBinding
import com.kolo.redditclone.interfaces.NetworkResponseInterface
import com.kolo.redditclone.models.MainModel
import com.kolo.redditclone.utils.AppConstants.BASE_URL
import com.kolo.redditclone.utils.AppUtils.newRequest
import org.json.JSONArray
import org.json.JSONObject

class PopularFragment : Fragment(), NetworkResponseInterface {
    private var _binding: FragmentPopularBinding? = null
    private val binding get() = _binding!!
    private var limit = 0
    private var isFirst = true
    private var isLoading = true
    private var isPaginate = false
    private var after = ""
    private var url = "/r/all/hot.json?"
    private var mainModel: ArrayList<MainModel> = arrayListOf()
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPopularBinding.inflate(inflater, container, false)

        val layout = LinearLayoutManager(requireContext())
        binding.rvPopular.apply {
            setHasFixedSize(true)
            layoutManager = layout
            adapter = PostAdapter(requireContext(), mainModel)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && !isLoading) {
                        mainModel.add(MainModel("", "", "", "", "", "", "", "", 0, 2))
                        binding.rvPopular.adapter!!.notifyDataSetChanged()
                        binding.rvPopular.scrollToPosition(mainModel.size - 1)
                        isPaginate = true
                        isLoading = true
                        loadData()
                    }
                }
            })
        }

        loadData()

        binding.txtPostType.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(it.rootView.context)
            val view = View.inflate(it.rootView.context, R.layout.post_type_bottom_sheet, null)

            val img1: ImageView = view.findViewById(R.id.img1)
            val img2: ImageView = view.findViewById(R.id.img2)
            val img3: ImageView = view.findViewById(R.id.img3)

            val hot: LinearLayout = view.findViewById(R.id.layoutHot)
            val new: LinearLayout = view.findViewById(R.id.layoutNew)
            val top: LinearLayout = view.findViewById(R.id.layoutTop)


            when (url) {
                "/r/all/hot.json?" -> img1.visibility = VISIBLE
                "/r/all/new.json?" -> img2.visibility = VISIBLE
                "/r/all/top.json?" -> img3.visibility = VISIBLE
            }

            hot.setOnClickListener {
                if (!isFirst) {
                    url = "/r/all/hot.json?"
                    reset()
                    binding.txtPostType.text = "HOT POSTS"
                    loadData()
                }
                bottomSheetDialog.cancel()
            }
            new.setOnClickListener {
                if (!isFirst) {
                    url = "/r/all/new.json?"
                    reset()
                    binding.txtPostType.text = "NEW POSTS"
                    loadData()
                }
                bottomSheetDialog.cancel()
            }
            top.setOnClickListener {
                if (!isFirst) {
                    url = "/r/all/top.json?"
                    reset()
                    binding.txtPostType.text = "TOP POSTS"
                    loadData()
                }
                bottomSheetDialog.cancel()
            }

            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }

        return binding.root
    }

    private fun reset() {
        after = ""
        mainModel.clear()
        binding.rvPopular.adapter!!.notifyDataSetChanged()
        isFirst = true
        binding.progressCircular.visibility = VISIBLE
        limit = 0
    }

    private fun loadData() {
        limit += 10
        requireContext().newRequest(BASE_URL  + "${url}limit=$limit&after=$after", this)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onResponse(response: String) {
        isLoading = false
        if (isFirst) {
            binding.layout.setBackgroundColor(Color.parseColor("#eeeeee"))
            isFirst = false
            binding.progressCircular.visibility = INVISIBLE
        }
        if (isPaginate) {
            isPaginate = false
            mainModel.removeAt(mainModel.size - 1)
        }
        val jsonObject = JSONObject(JSONObject(response).getString("data"))
        val data = JSONArray(jsonObject.getString("children"))
        after = jsonObject.getString("after")

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
        binding.rvPopular.adapter!!.notifyDataSetChanged()
    }

    override fun onError(error: VolleyError) {
        Log.d("Volley", error.message.toString())
    }

}