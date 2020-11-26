package com.example.djangotestapp.ui

import android.os.Bundle
import android.os.UserManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.djangotestapp.databinding.FragmentUserPostsBinding
import com.example.djangotestapp.model.api.Resource
import com.example.djangotestapp.model.dataClass.Post
import com.example.djangotestapp.ui.adapter.ViewPagerAdapter
import com.example.djangotestapp.utils.TopPadding
import com.example.djangotestapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_user_posts.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UsersPostPage : Fragment() {
    private val userViewModel: UserViewModel by viewModel()
    private lateinit var dataBinding: FragmentUserPostsBinding
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = FragmentUserPostsBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = this
        dataBinding.viewmodel = userViewModel
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs: com.example.djangotestapp.utils.UserManager? = dataBinding.viewmodel?.getPrefs()
        prefs?.userID?.asLiveData()?.observe(requireActivity(), Observer {
            if (it != null)
                dataBinding.viewmodel?.getUserPosts(it)
        })

        setupAdapter()
        setObservers()

    }

    private fun setObservers() {
        dataBinding.viewmodel?.postList?.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success->{
                    adapter.submitList(it.value as ArrayList<Post>)

                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Loading failure ${it}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun setupAdapter() {
        val viewModel = dataBinding.viewmodel
        if (viewModel != null) {
            adapter = ViewPagerAdapter()
            val layoutManager = LinearLayoutManager(requireContext())
            userPostsRecycler.layoutManager = layoutManager
            userPostsRecycler.addItemDecoration(
                TopPadding(30)
            )
            userPostsRecycler.adapter = adapter
        }
    }
}