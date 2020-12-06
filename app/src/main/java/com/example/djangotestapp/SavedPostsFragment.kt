package com.example.djangotestapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.djangotestapp.databinding.FragmentSavedPostsBinding
import com.example.djangotestapp.databinding.FragmentUserPostsBinding
import com.example.djangotestapp.model.api.Resource
import com.example.djangotestapp.model.dataClass.FavBody
import com.example.djangotestapp.model.dataClass.FavResponse
import com.example.djangotestapp.model.dataClass.Post
import com.example.djangotestapp.ui.PostListFragmentDirections
import com.example.djangotestapp.ui.adapter.FavPostAdapter
import com.example.djangotestapp.ui.adapter.OnPostClickListener
import com.example.djangotestapp.ui.adapter.ViewPagerAdapter
import com.example.djangotestapp.utils.TopPadding
import com.example.djangotestapp.viewmodel.PostViewModel
import com.example.djangotestapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_saved_posts.*
import kotlinx.android.synthetic.main.fragment_user_posts.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedPostsFragment : Fragment(),OnPostClickListener {
    private val postViewModel: PostViewModel by viewModel()
    private lateinit var dataBinding: FragmentSavedPostsBinding
    private lateinit var adapter: FavPostAdapter
    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding = FragmentSavedPostsBinding.inflate(inflater, container, false)
        dataBinding.lifecycleOwner = this
        dataBinding.viewmodel = postViewModel
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.BottomAB?.visibility = View.VISIBLE
        activity?.bottomNavView?.visibility = View.VISIBLE
        activity?.addPostBtn?.visibility = View.VISIBLE
        val prefs: com.example.djangotestapp.utils.UserManager? = dataBinding.viewmodel?.getPrefs()
        prefs?.userID?.asLiveData()?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                userId = it
                dataBinding.viewmodel?.getFavorites(it)
            }
        })

        setupAdapter()
        setObservers()
    }
    private fun setObservers() {

        postViewModel.favPosts?.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    adapter.submitFavitems(it.value as ArrayList<FavResponse>)
                }
                is Resource.Failure -> {
                    Toast.makeText(
                        requireContext(),
                        "Loading failure ${it}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        })

    }

    private fun setupAdapter() {
        val viewModel = dataBinding.viewmodel
        if (viewModel != null) {
            adapter = FavPostAdapter(this,dataBinding.viewmodel!!)
            val layoutManager = LinearLayoutManager(requireContext())
            savedPostsRecycler.layoutManager = layoutManager
            savedPostsRecycler.addItemDecoration(
                TopPadding(30)
            )
            savedPostsRecycler.adapter = adapter
        }
    }

    override fun onPostClick(post: Post) {
        view?.findNavController()
            ?.navigate(SavedPostsFragmentDirections.actionSavedPostsFragmentToPostDetailsFragment(post.id))
    }

    override fun setLike(postId: Int) {
        postViewModel.addToFav(FavBody(userId, true, postId))
    }

    override fun removeLike(postId: Int) {
        postViewModel.deleteFav(userId, postId)
    }
}