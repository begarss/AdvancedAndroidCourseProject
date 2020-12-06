package com.example.djangotestapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.djangotestapp.R
import com.example.djangotestapp.model.api.Resource
import com.example.djangotestapp.model.dataClass.PostCreateBody
import com.example.djangotestapp.utils.UserManager
import com.example.djangotestapp.viewmodel.PostViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_post.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddPostFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_add_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.BottomAB?.visibility = View.GONE
        activity?.bottomNavView?.visibility = View.GONE
        activity?.addPostBtn?.visibility = View.GONE
        setSpinner()
        newPostBtn.setOnClickListener {
            addPost()
            showResponse()
        }

    }

    fun addPost() {
        prefs = postViewModel.getPrefs()
        prefs.userID.asLiveData().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                userId = it
            }
        })
        val header = edHeader.text.toString()
        val body = edBody.text.toString()
        val cat = addPostSpinner.selectedItemPosition
        loadingBtnProgressAdd.visibility = View.VISIBLE

        postViewModel.addPost(PostCreateBody(userId, cat, body, header, false))


    }

    fun showResponse(){
                postViewModel.postResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    loadingBtnProgressAdd.visibility = View.GONE

                    Toasty.success(
                        requireContext(), "Новый пост добавлен на модерацию",
                        Toast.LENGTH_LONG, false
                    ).show()
                }
                is Resource.Failure -> {
                    loadingBtnProgressAdd.visibility = View.GONE
                    Toasty.error(requireContext(), "Ошибка при запросе", Toast.LENGTH_SHORT, false)
                        .show()
                }
            }
        })
    }

    fun setSpinner() {
        postViewModel.getCategories()
        postViewModel.categoryList.observe(viewLifecycleOwner, Observer {
            val dataAdapter =
                ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, it)
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            addPostSpinner.adapter = dataAdapter
            addPostSpinner.setSelection(0)
        })
    }
}