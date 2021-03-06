package com.example.djangotestapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.djangotestapp.R
import com.example.djangotestapp.utils.TopPadding
import com.example.djangotestapp.viewmodel.PostViewModel
import com.example.djangotestapp.databinding.FragmentPostListBinding
import com.example.djangotestapp.model.api.Resource
import com.example.djangotestapp.model.dataClass.FavBody
import com.example.djangotestapp.model.dataClass.FavResponse
import com.example.djangotestapp.model.dataClass.Post
import com.example.djangotestapp.ui.adapter.OnPostClickListener
import com.example.djangotestapp.ui.adapter.PostAdapter
import com.example.djangotestapp.utils.UserManager
import com.example.djangotestapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_post_list.*
import kotlinx.android.synthetic.main.post_item.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostListFragment : Fragment(), OnPostClickListener {
    private lateinit var viewDataBinding: FragmentPostListBinding
    private lateinit var adapter: PostAdapter
    private val postViewModel: PostViewModel by viewModel()
    private lateinit var prefs: UserManager
    private var userId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewDataBinding = FragmentPostListBinding.inflate(inflater, container, false)
        viewDataBinding.lifecycleOwner = this
        viewDataBinding.viewmodel = postViewModel
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.BottomAB?.visibility = View.VISIBLE
        activity?.bottomNavView?.visibility = View.VISIBLE
        activity?.addPostBtn?.visibility = View.VISIBLE



        viewDataBinding.viewmodel?.getCategories()
        setAdapter()
        fetchPosts()

        setSpinner()

        setFavPosts()


    }

    fun setFavPosts() {
        prefs = postViewModel.getPrefs()
        prefs.userID.asLiveData().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                userId = it
                postViewModel.
                getFavorites(it)
            }
        })
        postViewModel.favPosts.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    val markedPosts = it.value.map {
                        it.post_id
                    }
                    Log.d("UPP", "FAVVfetchPosts: $markedPosts  ")
                    adapter.submitFavs(markedPosts)
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun setSpinner() {
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewDataBinding.viewmodel?.postList?.observe(viewLifecycleOwner, Observer {
                    adapter.submitList(it as ArrayList<Post>)
                })
                adapter.notifyDataSetChanged()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                filterPost(position)
            }

        }
    }

    private fun setAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            adapter = PostAdapter(this, viewDataBinding.viewmodel!!)
            val layoutManager = LinearLayoutManager(requireContext())
            recycler.layoutManager = layoutManager
            recycler.addItemDecoration(
                TopPadding(30)
            )
            recycler.adapter = adapter

        }

    }


    private fun fetchPosts() {


        viewLifecycleOwner.lifecycleScope.launch {

            viewDataBinding.viewmodel?.fetchPosts()?.collectLatest {
                adapter.submitData(it)

            }
        }

    }

    private fun fetchCatPosts(position: Int) {

        viewLifecycleOwner.lifecycleScope.launch {

            viewDataBinding.viewmodel?.getCatPosts(position)?.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onPostClick(post: Post) {
        view?.findNavController()
            ?.navigate(PostListFragmentDirections.toPostDetailsFragment(post.id))

    }

    override fun setLike(postId: Int) {
        postViewModel.addToFav(FavBody(userId, true, postId))
    }

    override fun removeLike(postId: Int) {
        postViewModel.deleteFav(userId, postId)
    }

    private fun filterPost(position: Int) {


        if (position == 0) {
            viewLifecycleOwner.lifecycleScope.launch {

                viewDataBinding.viewmodel?.fetchPosts()?.collectLatest {
                    adapter.submitData(it)

                }
            }
            adapter.refresh()
        } else {
            fetchCatPosts(position)


        }
    }


}