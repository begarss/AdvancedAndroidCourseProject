package com.example.djangotestapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.djangotestapp.BR
import com.example.djangotestapp.R
import com.example.djangotestapp.databinding.FragmentPostDetailsBinding
import com.example.djangotestapp.viewmodel.PostViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class PostDetailsFragment : Fragment() {

    val arg: PostDetailsFragmentArgs by navArgs()
    private val postViewModel: PostViewModel by viewModel()
    private lateinit var postDetailsBinding: FragmentPostDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        postDetailsBinding = FragmentPostDetailsBinding.inflate(inflater, container, false).apply {
            viewmodel =
                postViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return postDetailsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arg.postId
        postDetailsBinding.viewmodel?.fetchPostDetails(id)
        setObservers()

    }

    private fun setObservers() {
        postDetailsBinding.viewmodel?.post?.observe(viewLifecycleOwner, Observer {
            var view = postDetailsBinding.root
            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'")
            val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
            val dater: Date = inputFormat.parse(it.date)
            val outputDateStr: String = outputFormat.format(dater)
            it.date = outputDateStr.toString()

            postDetailsBinding.setVariable(BR.POST,it)
            postDetailsBinding.executePendingBindings()
        })


    }
}

