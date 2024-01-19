package com.example.cinergyapp.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.cinergyapp.R
import com.example.cinergyapp.modal.GuestLoginRequest
import com.example.cinergyapp.modal.GuestLoginResponse
import com.example.cinergyapp.modal.GuestTokenRequest
import com.example.cinergyapp.modal.GuestTokenResponse
import com.example.cinergyapp.utils.AppPreference
import com.example.cinergyapp.utils.AppUtils
import com.example.cinergyapp.utils.DEVICE_TYPE
import com.example.cinergyapp.utils.DEVICE_UNIQUE_ID
import com.example.cinergyapp.utils.GUEST_TOKEN
import com.example.cinergyapp.utils.LOGIN_TYPE
import com.example.cinergyapp.utils.SECRET_KEY
import com.example.cinergyapp.utils.USER_ID
import com.example.cinergyapp.viewmodal.AppViewModal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
   private val continueAsGuest : Button by lazy { findViewById(R.id.continueAsGuest) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        System.out.println("MainActivityCalled:- " + "called....")
        val progressDialog : Dialog? = AppUtils.setProgressDialog(this@MainActivity)
        continueAsGuest.setOnClickListener {
            val viewModel: AppViewModal = AppViewModal()
            lifecycleScope.launch {
                progressDialog?.show()
                val deviceID = AppUtils.getUniqueDeviceId()
                println("GuestDataDeviceID:- $deviceID")
                val guestResp = try {
                    viewModel.getGuestTokenData(GuestTokenRequest(secret_key = SECRET_KEY ,
                        device_type = DEVICE_TYPE , device_id = deviceID , push_token = "test"))
                }catch (exception : Exception){
                    exception.printStackTrace()
                    progressDialog?.dismiss()
                }
                withContext(Dispatchers.IO){
                    println("GuestDataToken:- $guestResp")
                    if("success".equals((guestResp as? GuestTokenResponse)?.response , ignoreCase = true)){
                        val guestTokenResponse = guestResp as? GuestTokenResponse
                        AppPreference.saveString(DEVICE_UNIQUE_ID , deviceID ?: "")
                        AppPreference.saveString(GUEST_TOKEN , guestTokenResponse?.token ?: "")
                        val guestLoginResp = try {
                            viewModel.getGuestLoginData(guestTokenResponse?.token ?: "" , GuestLoginRequest(device_type = DEVICE_TYPE , device_id = deviceID , login_type = LOGIN_TYPE))
                        }catch (exception : Exception){
                            exception.printStackTrace()
                            progressDialog?.dismiss()
                        }
                        withContext(Dispatchers.Main){
                            progressDialog?.dismiss()
                            if("success".equals((guestLoginResp as? GuestLoginResponse)?.response , ignoreCase = true)){
                                val guestLoginResponse = guestLoginResp as? GuestLoginResponse
                                AppPreference.saveString(USER_ID , guestLoginResponse?.user?.id ?: "")
                                startActivity(Intent(this@MainActivity , EscapeRoomActivity::class.java))
                            }
                            println("GuestDataLogin:- $guestLoginResp")
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()
    }
}