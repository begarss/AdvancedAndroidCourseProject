package com.example.djangotestapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.ListFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.djangotestapp.R
import com.example.djangotestapp.viewmodel.UserViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment(),AppBarLayout.OnOffsetChangedListener {
    private val userViewModel: UserViewModel by viewModel()
    private val PERCENTAGE_TO_ANIMATE_AVATAR = 20
    private var mIsAvatarShown = true

    private var mProfileImage: ImageView? = null
    private var mMaxScrollSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout:TabLayout = materialup_tabs
        val viewPager:ViewPager = materialup_viewpager
        val appbarLayout:AppBarLayout = materialup_appbar
        mProfileImage = profilePic

        val toolbar :Toolbar= materialup_toolbar

        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

        appbarLayout.addOnOffsetChangedListener(this)
        mMaxScrollSize = appbarLayout.totalScrollRange

        viewPager.adapter = TabsAdapter(
            activity?.supportFragmentManager
        )
        tabLayout.setupWithViewPager(viewPager)

        val prefs: com.example.djangotestapp.utils.UserManager? = userViewModel.getPrefs()
        prefs?.userName?.asLiveData()?.observe(requireActivity(), Observer {
            if (it != null)
                userName.text = it.toString()
        })
        prefs?.userAva?.asLiveData()?.observe(requireActivity(), Observer {
            Glide.with(view.context).load(it).into(profilePic)
        })

    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, i: Int) {
        if (mMaxScrollSize == 0) mMaxScrollSize = appBarLayout!!.totalScrollRange

        val percentage: Int = Math.abs(i) * 100 / mMaxScrollSize

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false
            mProfileImage!!.animate()
                .scaleY(0f).scaleX(0f)
                .setDuration(200)
                .start()
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true
            mProfileImage!!.animate()
                .scaleY(1f).scaleX(1f)
                .start()
        }
    }

    private class TabsAdapter(fm: FragmentManager?) :
        FragmentPagerAdapter(fm!!) {
        override fun getCount(): Int {
            return TAB_COUNT
        }

        override fun getItem(i: Int): Fragment {
            return UsersPostPage()
        }

        override fun getPageTitle(position: Int): CharSequence? {
            if (position == 0)return "Published"
            else
                return "Not published"
        }

        companion object {
            private const val TAB_COUNT = 2
        }
    }

}