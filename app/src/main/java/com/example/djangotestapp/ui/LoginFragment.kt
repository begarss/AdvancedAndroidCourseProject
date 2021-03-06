package com.example.djangotestapp.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
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
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
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


                    userViewModel.saveUserInfo(
                        it.value.token,
                        it.value.username,
                        it.value.userid,
                        it.value.profile_pic ?:"",
                        it.value.is_superuser
                    )
                    Toast.makeText(
                        requireContext(),
                        "Login success ${it.value.username}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        requireActivity().startNewActivity(MainActivity::class.java)
                    },300)

                    loadingBtnProgress.visibility = View.GONE



                }
                is Resource.Failure -> {
                    showError(it.errorBody)
                }
            }
        })

        btnLogin.setOnClickListener {
            val username = loginTF.text.toString()
            val password = loginPsw.text.toString()
            loadingBtnProgress.visibility = View.VISIBLE
            userViewModel.login(username, password)
        }
        moveToSignUp(textViewRegisterNow.text.toString())

    }

    fun moveToSignUp(text:String){
        val ss = SpannableString(text)

        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment2())
            }
        }
        Log.d("UPP", "moveToSignUp: $text")
        ss.setSpan(clickableSpan1, 25, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textViewRegisterNow.text =ss
        textViewRegisterNow.movementMethod = LinkMovementMethod.getInstance()
    }

    fun showError(errorBody: ResponseBody?){
        var jsonObject: JSONObject? = null

        try {
            jsonObject = JSONObject(errorBody?.string())
            val userMessage = jsonObject!!
                .getJSONArray("non_field_errors")[0]

            Toasty.error(requireContext(), "$userMessage", Toast.LENGTH_SHORT, true)
                .show();
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