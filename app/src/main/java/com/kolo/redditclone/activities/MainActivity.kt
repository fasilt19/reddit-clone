package com.kolo.redditclone.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kolo.redditclone.R
import com.kolo.redditclone.databinding.ActivityMainBinding
import com.kolo.redditclone.fragments.ChatsFragment
import com.kolo.redditclone.fragments.HomeFragment
import com.kolo.redditclone.fragments.NotificationsFragment
import com.kolo.redditclone.fragments.SearchFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.viewPager.apply {
            isUserInputEnabled = false
            adapter = PagerAdapter(supportFragmentManager, lifecycle)
        }

        binding.bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home-> binding.viewPager.setCurrentItem(0,false)
                R.id.explore-> binding.viewPager.setCurrentItem(1,false)
                R.id.add-> {
                    return@setOnNavigationItemSelectedListener false
                }
                R.id.chats-> binding.viewPager.setCurrentItem(2,false)
                R.id.notifications-> binding.viewPager.setCurrentItem(3,false)
            }
            true
        }

    }

    inner class PagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {
        override fun getItemCount(): Int {
            return 4
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> SearchFragment()
                2 -> ChatsFragment()
                else -> NotificationsFragment()
            }
        }

    }
}