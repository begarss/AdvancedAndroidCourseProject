package com.example.djangotestapp.utils

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.djangotestapp.ui.MainActivity

abstract class BaseFragment : Fragment() {

//    protected open var bottomNavigationViewVisibility = View.VISIBLE
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        // get the reference of the parent activity and call the setBottomNavigationVisibility method.
//        if (activity is MainActivity) {
//            var  mainActivity = activity as MainActivity
//
//            mainActivity.setBottomNavigationVisibility(bottomNavigationViewVisibility)
//        }
//    }
//    override fun onResume() {
//        super.onResume()
//        if (activity is MainActivity) {
//            var  mainActivity = activity as MainActivity
//            mainActivity.setBottomNavigationVisibility(bottomNavigationViewVisibility)
//        }
//    }
}