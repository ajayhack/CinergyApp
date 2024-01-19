package com.example.cinergyapp.view

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinergyapp.R
import com.example.cinergyapp.modal.EscapeRoomMoviesRequest
import com.example.cinergyapp.modal.EscapeRoomMoviesResponse
import com.example.cinergyapp.modal.RoomData
import com.example.cinergyapp.utils.AppPreference
import com.example.cinergyapp.utils.AppUtils
import com.example.cinergyapp.utils.DEVICE_TYPE
import com.example.cinergyapp.utils.DEVICE_UNIQUE_ID
import com.example.cinergyapp.utils.GUEST_TOKEN
import com.example.cinergyapp.utils.LOCATION_ID
import com.example.cinergyapp.utils.USER_ID
import com.example.cinergyapp.viewmodal.AppViewModal
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EscapeRoomActivity : AppCompatActivity() {
    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) }
    private val backArrow: ImageView by lazy { findViewById(R.id.backArrow) }
    private val toolbarTitle: TextView by lazy { findViewById(R.id.toolbarTitle) }
    private val escapeRoomRV: RecyclerView by lazy { findViewById(R.id.recyclerView) }
    private var bottomSheetDialog : BottomSheetDialog? = null
    private var roomMovieList : MutableList<RoomData>? = null
    private var erTickets : String? = null
    private val escapeRoomMoviesAdapter by lazy { EscapeRoomMoviesAdapter(roomMovieList ,  ::onItemClick, this@EscapeRoomActivity) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.escape_room_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }
        toolbar.elevation = 0f
        val progressDialog: Dialog? = AppUtils.setProgressDialog(this@EscapeRoomActivity)
        backArrow.setOnClickListener { this.finishAffinity() }

        val viewModel: AppViewModal = AppViewModal()
        lifecycleScope.launch {
            progressDialog?.show()
            val deviceId = AppPreference.getString(DEVICE_UNIQUE_ID)
            val userId = AppPreference.getString(USER_ID)
            val token = AppPreference.getString(GUEST_TOKEN)
            val escapeRoomResp = try {
                viewModel.getEscapeMoviesData(
                    token ?: "", userId ?: "",
                    EscapeRoomMoviesRequest(
                        device_id = deviceId,
                        member_id = "",
                        device_type = DEVICE_TYPE,
                        location_id = LOCATION_ID
                    )
                )
            } catch (exception: Exception) {
                exception.printStackTrace()
                progressDialog?.dismiss()
            }

            withContext(Dispatchers.Main){
                progressDialog?.dismiss()
                if("success".equals((escapeRoomResp as? EscapeRoomMoviesResponse)?.response , ignoreCase = true)){
                    println("EscapeData:- " + escapeRoomResp as? EscapeRoomMoviesResponse)
                    roomMovieList = (escapeRoomResp as? EscapeRoomMoviesResponse)?.escape_rooms_movies
                    erTickets = (escapeRoomResp as? EscapeRoomMoviesResponse)?.er_tickets
                    escapeRoomRV.apply {
                        adapter = escapeRoomMoviesAdapter
                        layoutManager = GridLayoutManager(this@EscapeRoomActivity , 2)
                        itemAnimator = DefaultItemAnimator()
                    }
                }
            }
        }
    }

    //region=================On Item click event Handle:-
    private fun onItemClick(position : Int){
        if(position > -1){
            println("Position:- $position")
            showBottomSheet(position)
        }
    }
    //endregion

    private fun showBottomSheet(position: Int){
        bottomSheetDialog = BottomSheetDialog(this@EscapeRoomActivity, R.style.AppBottomSheetDialogTheme)
        val sheetView: View = LayoutInflater.from(this).inflate(R.layout.bottom_sheet, this.window.decorView.rootView as ViewGroup, false)
        bottomSheetDialog?.setContentView(sheetView)
        val mBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(sheetView.parent as View)
        mBehavior.isFitToContents = false
        mBehavior.halfExpandedRatio = 0.5f
        mBehavior.isDraggable = false

        val escapeRoomIV = bottomSheetDialog?.findViewById<ImageView>(R.id.escapeRoomIV)
        val closeIcon = bottomSheetDialog?.findViewById<ImageView>(R.id.closeIcon)
        val escapeRoomTV = bottomSheetDialog?.findViewById<TextView>(R.id.escapeRoomTV)
        val timeTV = bottomSheetDialog?.findViewById<TextView>(R.id.timeTV)
        val ticketsTV = bottomSheetDialog?.findViewById<TextView>(R.id.ticketsTV)
        val synopsisTV = bottomSheetDialog?.findViewById<TextView>(R.id.synopsisTV)
        val continueButton = bottomSheetDialog?.findViewById<Button>(R.id.continueButton)

        if(!roomMovieList?.get(position)?.image_url.isNullOrEmpty() && escapeRoomIV != null){
            Glide.with(this@EscapeRoomActivity).load(roomMovieList?.get(position)?.image_url).into(escapeRoomIV)
        }
        if(!roomMovieList?.get(position)?.Title.isNullOrEmpty()){
            escapeRoomTV?.text = roomMovieList?.get(position)?.Title
        }else {
            escapeRoomTV?.visibility = View.GONE
        }

        if(!roomMovieList?.get(position)?.RunTime.isNullOrEmpty()){
            timeTV?.text = roomMovieList?.get(position)?.RunTime + "mins"
        }else {
            timeTV?.visibility = View.GONE
        }

        if(!erTickets.isNullOrEmpty()){
            ticketsTV?.text = erTickets
        }else{
            ticketsTV?.visibility = View.GONE
        }

        if(!roomMovieList?.get(position)?.Synopsis.isNullOrEmpty()){
            synopsisTV?.text = roomMovieList?.get(position)?.Synopsis
        }else {
            synopsisTV?.visibility = View.GONE
        }

        closeIcon?.setOnClickListener {
            bottomSheetDialog?.dismiss()
        }

        continueButton?.setOnClickListener {
            bottomSheetDialog?.dismiss()
            startActivity(Intent(this@EscapeRoomActivity , EscapeRoomScheduleActivity::class.java).apply {
                putExtra("movieID" , roomMovieList?.get(position)?.ID)
            })
        }

        bottomSheetDialog?.setOnShowListener { dialog: DialogInterface? ->
            mBehavior.setPeekHeight(sheetView.height)
        }
        bottomSheetDialog?.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(bottomSheetDialog?.isShowing == true){
            bottomSheetDialog?.dismiss()
        }else{
            this.finishAffinity()
        }
    }
}

class EscapeRoomMoviesAdapter(private var escapeRoomMoviesList : MutableList<RoomData>?,
                                  private var onItemClickEvent: (Int) -> Unit,
                                  private val mContext: Context) :
    RecyclerView.Adapter<EscapeRoomMoviesAdapter.EscapeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EscapeViewHolder {
        return EscapeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.escape_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: EscapeViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int = escapeRoomMoviesList?.size ?: 0

    inner class EscapeViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val container = itemView.findViewById<ConstraintLayout>(R.id.container)
        val escapeRoomIV = itemView.findViewById<ImageView>(R.id.escapeRoomIV)
        val escapeRoomTV = itemView.findViewById<TextView>(R.id.escapeRoomTV)

        fun bindData(position: Int){
            val model = escapeRoomMoviesList?.get(position)
            if(!model?.image_url.isNullOrEmpty()){
                Glide.with(mContext).load(model?.image_url).into(escapeRoomIV)
            }
            if(!model?.Title.isNullOrEmpty()){
                escapeRoomTV.text = model?.Title
            }
        }

        init {
            container.setOnClickListener { onItemClickEvent(adapterPosition) }
        }
    }
}