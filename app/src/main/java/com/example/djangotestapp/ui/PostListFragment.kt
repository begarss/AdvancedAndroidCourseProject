package com.example.djangotestapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.djangotestapp.utils.TopPadding
import com.example.djangotestapp.viewmodel.PostViewModel
import com.example.djangotestapp.databinding.FragmentPostListBinding
import com.example.djangotestapp.model.dataClass.Post
import com.example.djangotestapp.ui.adapter.OnPostClickListener
import com.example.djangotestapp.ui.adapter.PostAdapter
import kotlinx.android.synthetic.main.fragment_post_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class PostListFragment : Fragment(), OnPostClickListener {
    private lateinit var viewDataBinding: FragmentPostListBinding
    private lateinit var adapter: PostAdapter
    private val postViewModel: PostViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewDataBinding = FragmentPostListBinding.inflate(inflater, container, false)
        viewDataBinding.lifecycleOwner=this
        viewDataBinding.viewmodel = postViewModel
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewDataBinding.viewmodel?.getCategories()
        setAdapter()
//        setObserver()
        fetchPosts()




            Log.d("UPP", "fetchPosts: ${adapter.getList()}")


        setSpinner()
        Log.d("UPP", "fetchPostsasa: ${adapter.getItemViewType(1)}")


    }

    private fun setSpinner(){
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

//    private fun setObserver() = lifecycleScope.launch {
//        viewDataBinding.viewmodel?.fetchPosts()?.collectLatest {
//            adapter.submitData(it)
//            Log.d("UPP", "setObserver: ${it.map { it }}")
//        }
//
//
//    }

    private fun fetchPosts() {
        viewLifecycleOwner.lifecycleScope.launch {

         viewDataBinding?.viewmodel?.fetchPosts()?.collectLatest {
             adapter.submitData(it)
         }
            Log.d("UPP", "fetchPostsss: ${adapter.getItemId(1)}")

        }

    }
//        viewDataBinding.viewmodel?.postList?.observe(viewLifecycleOwner, Observer {
//            adapter.submitList(it as ArrayList<Post>)
//        })

    override fun onPostClick(position: Int, view: View) {
        val post = adapter.getList()[position]
        view.findNavController().navigate(PostListFragmentDirections.toPostDetailsFragment(post.id))

    }

    private fun filterPost(position: Int) {


        if (position == 0) {
            viewDataBinding.viewmodel?.postList?.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it as ArrayList<Post>)
            })
        } else {
            viewDataBinding?.viewmodel?.getCatPosts(position)

            viewDataBinding.viewmodel?.categoryPosts?.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it as ArrayList<Post>)
            })
        }
    }


}