package com.example.themovieapps.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.themovieapps.R
import com.example.themovieapps.fragment.MovieFavorite
import com.example.themovieapps.fragment.MovieFragment
import com.example.themovieapps.fragment.SerialFavorite
import com.example.themovieapps.fragment.SerialFragment

class SectionPagerAdapter(private val mContext: Context?, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val titlesHeader = intArrayOf(
        R.string.title_1,
        R.string.title_2
    )

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = MovieFavorite()
            1 -> fragment = SerialFavorite()
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext?.resources?.getString(titlesHeader[position])
    }

    override fun getCount(): Int {
        return titlesHeader.size
    }

}