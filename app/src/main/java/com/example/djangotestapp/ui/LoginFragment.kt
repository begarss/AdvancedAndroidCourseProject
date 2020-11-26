package com.example.djangotestapp.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.example.djangotestapp.R
import com.example.djangotestapp.model.api.Resource
import com.example.djangotestapp.utils.startNewActivity
import com.example.djangotestapp.viewmodel.UserViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {


    private val userViewModel: UserViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userViewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    Toast.makeText(
                        requireContext(),
                        "Login success ${it.value.username}",
                        Toast.LENGTH_SHORT
                    ).show()

                    userViewModel.saveUserInfo(
                        it.value.token,
                        it.value.username,
                        it.value.userid,
                        it.value.profile_pic,
                        it.value.is_superuser
                    )
                    requireActivity().startNewActivity(MainActivity::class.java)



                }
                is Resource.Failure -> {
                    Toast.makeText(requireContext(), "Login failure ${it}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        btnLogin.setOnClickListener {
            val username = loginTF.text.toString()
            val password = loginPsw.text.toString()
            loadingBtnProgress.visibility = View.VISIBLE
            userViewModel.login(username, password)
        }


    }


}