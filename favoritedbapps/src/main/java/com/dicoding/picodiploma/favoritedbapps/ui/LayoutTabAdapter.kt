package com.dicoding.picodiploma.favoritedbapps.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dicoding.picodiploma.favoritedbapps.R
import com.dicoding.picodiploma.favoritedbapps.ui.movie.MovieFragment
import com.dicoding.picodiploma.favoritedbapps.ui.serial.SerialFragment

class LayoutTabAdapter(private val context: Context, fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val titleHeader = intArrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
    )

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = MovieFragment()
            1 -> fragment = SerialFragment()
        }
        return fragment as Fragment
    }

    override fun getCount(): Int = titleHeader.size

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(titleHeader[position])
    }
}