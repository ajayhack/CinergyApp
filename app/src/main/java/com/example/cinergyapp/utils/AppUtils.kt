package com.example.cinergyapp.utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.example.cinergyapp.R
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

const val GUEST_TOKEN = "guestToken"
const val USER_ID = "userId"
const val DEVICE_UNIQUE_ID = "deviceUniqueId"
const val SECRET_KEY = "402620C92552D9303C58B901B43B0A41718E772C19520DD9A9AA52CE5A8FCB99"
const val DEVICE_TYPE = "2"
const val LOGIN_TYPE = "2"
const val LOCATION_ID = "5"
object AppUtils {
    private var progressDialog : Dialog? = null
    fun getUniqueDeviceId() : String {
        return (System.currentTimeMillis() / 1000).toFloat().roundToInt().toString() + "CI-"
    }

    //region==========setup Progress Dialog:-
    fun setProgressDialog(activity : Activity) : Dialog? {
         progressDialog = Dialog(activity).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.item_progress_dialog)
            setCancelable(false)
        }
        return progressDialog as Dialog
    }
    //endregion

    //region==========Format Date:-
    fun formatDate(date : String? = null) : String? {
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd" , Locale.getDefault())
        val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy" , Locale.getDefault())
        var formattedDate: String? = null
        try {
            if(!date.isNullOrEmpty()){
                val parsedDate = inputFormat.parse(date)
                formattedDate = parsedDate?.let { outputFormat.format(it) }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return formattedDate
    }
    //endregion
}