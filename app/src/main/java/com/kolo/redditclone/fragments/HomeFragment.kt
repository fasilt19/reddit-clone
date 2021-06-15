package com.kolo.redditclone.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.kolo.redditclone.R
import com.kolo.redditclone.activities.SearchActivity
import com.kolo.redditclone.databinding.FragmentHomeBinding
import com.kolo.redditclone.models.SearchModel
import com.kolo.redditclone.utils.AppUtils.hideKeyboard
import com.kolo.redditclone.utils.DatabaseBuilder


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var searchModel: ArrayList<SearchModel> = arrayListOf()
    private var list: ArrayList<String> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val db = DatabaseBuilder.getInstance(requireContext())

        searchModel.addAll(db.SearchDao().getAll())

        searchModel.forEach {
            list.add(it.query)
        }

        binding.viewPager.apply {
            adapter = PagerAdapter(childFragmentManager, lifecycle)
            TabLayoutMediator(binding.tabLayout, this) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "Home"
                    }
                    1 -> {
                        tab.text = "Popular"
                    }
                }
                binding.viewPager.setCurrentItem(tab.position, false)
            }.attach()
        }

        binding.edtSearch.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                list
            )
        )



        binding.edtSearch.setOnEditorActionListener { _, p1, _ ->
            if (p1 == EditorInfo.IME_ACTION_SEARCH) {
                if (!db.SearchDao().isExist(binding.edtSearch.text.toString())) {
                    val model = SearchModel(0, binding.edtSearch.text.toString())
                    db.SearchDao().insert(model)
                }
                hideKeyboard(requireContext(), binding.root)
                startActivity(
                    Intent(requireContext(), SearchActivity::class.java).putExtra(
                        "query",
                        binding.edtSearch.text.toString()
                    )
                )
                binding.edtSearch.text.clear()
            }
            false
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    inner class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TabHomeFragment()
                else -> PopularFragment()
            }
        }
    }
}