package com.example.djangotestapp.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.djangotestapp.R
import com.example.djangotestapp.model.api.Resource
import com.example.djangotestapp.model.dataClass.Post
import com.example.djangotestapp.utils.UserManager
import com.example.djangotestapp.utils.startNewActivity
import com.example.djangotestapp.viewmodel.UserViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment(),AppBarLayout.OnOffsetChangedListener {
    private val userViewModel: UserViewModel by viewModel()
    private val PERCENTAGE_TO_ANIMATE_AVATAR = 20
    private var mIsAvatarShown = true

    private var mProfileImage: ImageView? = null
    private var mMaxScrollSize = 0
    private lateinit var  prefs:UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)


    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v: View = LayoutInflater.from(container?.context)
            .inflate(R.layout.fragment_profile, container, false)
        val toolbar = v.findViewById(R.id.materialup_toolbar) as Toolbar
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        toolbar.title=""

        return v
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout:TabLayout = materialup_tabs
        val viewPager:ViewPager = materialup_viewpager
        val appbarLayout:AppBarLayout = materialup_appbar
        mProfileImage = profilePic
        appbarLayout.addOnOffsetChangedListener(this)
        mMaxScrollSize = appbarLayout.totalScrollRange

        viewPager.adapter = TabsAdapter(
            activity?.supportFragmentManager
        )
        tabLayout.setupWithViewPager(viewPager)

        prefs = userViewModel.getPrefs()
        setPostsCount()
        prefs.userName.asLiveData().observe(requireActivity(), Observer {
            if (it != null)
                userName.text = it.toString()
        })
        prefs.userAva.asLiveData().observe(requireActivity(), Observer {
            if (it!=null)
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
        FragmentStatePagerAdapter(fm!!) {
        override fun getCount(): Int {
            return TAB_COUNT
        }

        override fun getItem(i: Int): Fragment {
            return UsersPostPage()
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return if (position == 0) "Published"
            else
                "Not published"
        }

        companion object {
            private const val TAB_COUNT = 2
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.action_logout){
            logout()
        }else
            return super.onOptionsItemSelected(item)
        return true
    }

    private fun logout() = lifecycleScope.launch {
        prefs.clear()
        requireActivity().startNewActivity(LoginActivity::class.java)
    }

    private fun setPostsCount(){
        prefs.postCount.asLiveData().observe(requireActivity(), Observer {
            if (it!=null)
                postCount.text = it.toString()
        })
    }
}