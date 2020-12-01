package com.example.djangotestapp.ui

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.example.djangotestapp.R
import com.example.djangotestapp.model.api.Resource
import com.example.djangotestapp.model.dataClass.UserCreateBody
import com.example.djangotestapp.utils.LoadingDialog
import com.example.djangotestapp.utils.UserManager
import com.example.djangotestapp.viewmodel.UserViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.loadingBtnProgress
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class SettingsFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    private val userViewModel: UserViewModel by viewModel()
    private var mProfileImage: ImageView? = null
    private lateinit var prefs: UserManager
    private val STORAGE_PERMISSION_CODE = 1
    private var userId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mProfileImage = setIV
        setUserInfo()
        btnSetAva.setOnClickListener {
            openCamera()
        }

        btnChange.setOnClickListener{
            editUser()
            showResponse()
        }
        
        newPswCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                newPsw.visibility = View.VISIBLE
            else
                newPsw.visibility = View.GONE
        }

    }

    fun setUserInfo() {
        prefs = userViewModel.getPrefs()
        prefs.userName.asLiveData().observe(requireActivity(), Observer {
            if (it != null)
                Setlogin.setText(it.toString())
        })
        prefs.userAva.asLiveData().observe(requireActivity(), Observer {
            if (it != null)
                Glide.with(requireContext()).load(it).into(setIV)
        })

        prefs.userID.asLiveData().observe(requireActivity(), Observer {
            if (it != null)
                userId = it
        })

    }

    private fun openCamera() {
        val perms = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (EasyPermissions.hasPermissions(requireActivity(), *perms)) {
//            Toasty.info(requireContext(), "Camera opens", Toast.LENGTH_SHORT, true)
//                .show();
            CropImage.activity()
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(requireContext(), this)
        } else {
            EasyPermissions.requestPermissions(
                this, "To select photo to your avatar we need that permissions",
                STORAGE_PERMISSION_CODE, *perms
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val res = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                res.uri?.let {
                    setImage(it)
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val e: Exception = res.error
                Toast.makeText(requireActivity(), "Error is $e", Toast.LENGTH_SHORT).show()
            }

        }
    }



    private fun setImage(imageUri: Uri) {
        Glide.with(this).load(imageUri).into(setIV)
        userViewModel.saveUserAva(imageUri.toString())
        val file = File(imageUri.path)

        val filePart = MultipartBody.Part.createFormData(
            "profile_pic",
            file.name,
            file.asRequestBody(
                "image/*".toMediaType()
            )
        )
        Log.d("UPP", "setImageContextResolver: ${"image/*".toMediaTypeOrNull()}")

        userViewModel.setUserAva(userId, filePart)

    }
    
    

    private fun editUser() {
        val name = Setlogin.text.toString()
        val psw = Setpsw.text.toString()
        var info : UserCreateBody
        if (!newPswCheckBox.isChecked){
            if (psw.isNotEmpty()){
                userViewModel.editUser(userId,UserCreateBody(name,psw))
                loadingBtnProgress.visibility = View.VISIBLE

            }else{
                settingsPsw.error = "Пароль не должен быть пустым"
            }
        }else{
            val newPassword = newPsw.editText?.text.toString()
            if (newPassword.isEmpty())
                newPsw.error = "Новый пароль не должен быть пустым"
            else {
                loadingBtnProgress.visibility = View.VISIBLE
                userViewModel.editUser(userId,UserCreateBody(name,newPassword))
            }
        }
    }

    private fun showResponse(){
        userViewModel.editResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success->{
                    loadingBtnProgress.visibility = View.GONE
                    newPswCheckBox.isChecked = false
                    userViewModel.saveUserName(it.value.username)
                    Toasty.success(requireContext(),"Вы упешно изменили данные",Toast.LENGTH_LONG,false).show()
                }
                is Resource.Failure -> {
                    loadingBtnProgress.visibility = View.GONE
                    Toasty.error(requireContext(),"Ошибка при запросе",Toast.LENGTH_SHORT,false).show()
                }
                Resource.Loading -> TODO()
            }
        })
    }
}