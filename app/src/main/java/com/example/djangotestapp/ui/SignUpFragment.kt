package com.example.djangotestapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.engine.Resource
import com.example.djangotestapp.R
import com.example.djangotestapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_sign_up.*
import org.json.JSONException
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel


class SignUpFragment : Fragment() {
    private val userViewModel: UserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnCreate.setOnClickListener {
            loadingBtnProgress.visibility = View.VISIBLE
            val username = SUlogin.text.toString()
            val psw = SUpsw.text.toString()
            userViewModel.createUser(username, psw)
        }

        userViewModel.newUserResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is com.example.djangotestapp.model.api.Resource.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "User created ${it.value.username}",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadingBtnProgress.visibility = View.GONE

                    findNavController().popBackStack()
                }
                is com.example.djangotestapp.model.api.Resource.Failure -> {
                    loadingBtnProgress.visibility = View.GONE
                    var jsonObject: JSONObject? = null

                    try {
                        jsonObject = JSONObject(it.errorBody?.string())
                        val userMessage = jsonObject!!
                            .getJSONArray("username")[0]


                        Toast.makeText(requireContext(), "${userMessage}", Toast.LENGTH_SHORT)
                            .show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(
                            requireContext(),
                            "User creation failure ${e.toString()}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }

                }
            }
        })
    }

}