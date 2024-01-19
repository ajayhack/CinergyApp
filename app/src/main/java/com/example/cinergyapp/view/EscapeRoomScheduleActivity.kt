package com.example.cinergyapp.view

import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cinergyapp.R
import com.example.cinergyapp.modal.ChooseDateModal
import com.example.cinergyapp.modal.MovieInfoResponse
import com.example.cinergyapp.modal.MoviesInfoRequest
import com.example.cinergyapp.modal.Session
import com.example.cinergyapp.utils.AppPreference
import com.example.cinergyapp.utils.AppUtils
import com.example.cinergyapp.utils.DEVICE_TYPE
import com.example.cinergyapp.utils.DEVICE_UNIQUE_ID
import com.example.cinergyapp.utils.GUEST_TOKEN
import com.example.cinergyapp.utils.LOCATION_ID
import com.example.cinergyapp.utils.USER_ID
import com.example.cinergyapp.viewmodal.AppViewModal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EscapeRoomScheduleActivity : AppCompatActivity() {
    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) }
    private val backArrow: ImageView by lazy { findViewById(R.id.backArrow) }
    private val toolbarTitle: TextView by lazy { findViewById(R.id.toolbarTitle) }
    private val escapeRoomIV: ImageView by lazy { findViewById(R.id.escapeRoomIV) }
    private val escapeRoomTV: TextView by lazy { findViewById(R.id.escapeRoomTV) }
    private val chooseDatePlaceholder: TextView by lazy { findViewById(R.id.chooseDatePlaceholder) }
    private val chooseTimePlaceholder: TextView by lazy { findViewById(R.id.chooseTimePlaceholder) }
    private val escapeRoomRatingTimeTV: TextView by lazy { findViewById(R.id.escapeRoomRatingTimeTV) }
    private val dateRecyclerview: RecyclerView by lazy { findViewById(R.id.dateRecyclerview) }
    private val timeRecyclerview: RecyclerView by lazy { findViewById(R.id.timeRecyclerview) }
    private val continueButton: Button by lazy { findViewById(R.id.continueButton) }
    private var dateList : MutableList<ChooseDateModal>? = mutableListOf()
    private var timeList : MutableList<Session>? = mutableListOf()
    private val dateAdapter by lazy { ChooseDateAdapter(dateList ,  ::onDateItemClick, this@EscapeRoomScheduleActivity) }
    private var movieID : String? = null
    private var movieInfo : MovieInfoResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.escape_room_schedule_activity)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }
        toolbar.elevation = 0f
        if(intent.extras != null && !intent.getStringExtra("movieID").isNullOrEmpty()){
            movieID = intent.getStringExtra("movieID")
            println("MovieID:- $movieID")
        }
        val progressDialog: Dialog? = AppUtils.setProgressDialog(this@EscapeRoomScheduleActivity)
        backArrow.setOnClickListener { onBackPressed() }

        val viewModel: AppViewModal = AppViewModal()
        lifecycleScope.launch {
            progressDialog?.show()
            val deviceId = AppPreference.getString(DEVICE_UNIQUE_ID)
            val userId = AppPreference.getString(USER_ID)
            val token = AppPreference.getString(GUEST_TOKEN)
            val movieInfoData = try {
                viewModel.getMovieInfoData(
                    token ?: "", userId ?: "",
                    MoviesInfoRequest(
                        device_id = deviceId,
                        device_type = DEVICE_TYPE,
                        location_id = LOCATION_ID,
                        movie_id = movieID
                    )
                )
            } catch (exception: Exception) {
                exception.printStackTrace()
                progressDialog?.dismiss()
            }

            withContext(Dispatchers.Main){
                if("success".equals((movieInfoData as? MovieInfoResponse)?.response , ignoreCase = true)){
                    println("MovieInfo:- " + movieInfoData as? MovieInfoResponse)
                    movieInfo = (movieInfoData as? MovieInfoResponse)
                    if(!movieInfo?.movie_info?.date_list.isNullOrEmpty()){
                        for(i in 0 until movieInfo?.movie_info?.date_list?.size!!){
                            dateList?.add(ChooseDateModal(movieInfo?.movie_info?.date_list?.get(i) , false))
                        }
                    }
                    if(!movieInfo?.movie_info?.image_url.isNullOrEmpty()){
                        Glide.with(this@EscapeRoomScheduleActivity).load(movieInfo?.movie_info?.image_url).into(escapeRoomIV)
                    }else{
                        escapeRoomIV.visibility = View.GONE
                    }

                    if(!movieInfo?.movie_info?.Title.isNullOrEmpty()){
                        escapeRoomTV.text = movieInfo?.movie_info?.Title
                    }else{
                        escapeRoomTV.visibility = View.GONE
                    }
                    var movieRatingTimeText : String? = null
                    if(!movieInfo?.movie_info?.Rating.isNullOrEmpty()){
                        movieRatingTimeText = movieInfo?.movie_info?.Rating + " * "
                    }
                    if(!movieInfo?.movie_info?.RunTime.isNullOrEmpty()){
                        movieRatingTimeText = movieRatingTimeText + movieInfo?.movie_info?.RunTime + " MIN"
                    }
                    if(!movieRatingTimeText.isNullOrEmpty()){
                        escapeRoomRatingTimeTV.text = movieRatingTimeText
                    }else{
                        escapeRoomRatingTimeTV.visibility = View.GONE
                    }
                    delay(1000)
                    dateRecyclerview.apply {
                        adapter = dateAdapter
                        layoutManager = LinearLayoutManager(this@EscapeRoomScheduleActivity , LinearLayoutManager.HORIZONTAL , false)
                        itemAnimator = DefaultItemAnimator()
                    }

                    if(!dateList.isNullOrEmpty()){
                        chooseDatePlaceholder.visibility = View.VISIBLE
                    }
                }
                progressDialog?.dismiss()
            }
        }
    }

    //region=================On Item click event Handle:-
    private fun onDateItemClick(position : Int){
        if(position > -1 && !dateList.isNullOrEmpty()){
            println("Position:- $position")
            for (i in 0 until dateList?.size!!){
                dateList?.get(i)?.isSelected = false
            }
            dateList?.get(position)?.isSelected = true
            dateAdapter.notifyDataSetChanged()

            timeList = movieInfo?.movie_info?.show_times?.firstOrNull{ it.date == dateList?.get(position)?.date }?.sessions
            if(!timeList.isNullOrEmpty()){
                chooseTimePlaceholder.visibility = View.VISIBLE
                continueButton.visibility = View.VISIBLE
                timeRecyclerview.apply {
                    adapter = TimeAdapter(timeList ,  ::onDateItemClick, this@EscapeRoomScheduleActivity)
                    layoutManager = GridLayoutManager(this@EscapeRoomScheduleActivity , 3)
                    itemAnimator = DefaultItemAnimator()
                }
            }
        }
    }
    //endregion

    //region=================On Item click event Handle:-
    private fun onTimeItemClick(position : Int){
        if(position > -1 && !dateList.isNullOrEmpty()){
            println("Position:- $position")
        }
    }
    //endregion

}

//region=================Date Adapter
class ChooseDateAdapter(private var chooseDateList : MutableList<ChooseDateModal>?,
                              private var onItemClickEvent: (Int) -> Unit,
                              private val mContext: Context
) :
    RecyclerView.Adapter<ChooseDateAdapter.DateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        return DateViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.choose_date_list, parent, false))
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int = chooseDateList?.size ?: 0

    inner class DateViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val container = itemView.findViewById<ConstraintLayout>(R.id.container)
        val dateTV = itemView.findViewById<TextView>(R.id.dateTV)

        fun bindData(position: Int){
            val date = chooseDateList?.get(position)?.date
            if(!date.isNullOrEmpty()){
               dateTV.text = AppUtils.formatDate(date)
                if(chooseDateList?.get(position)?.isSelected == true){
                    dateTV.setTextColor(mContext.getColor(R.color.appButtonColor))
                }else{
                    dateTV.setTextColor(mContext.getColor(R.color.black))
                }
            }
        }

        init {
            container.setOnClickListener {
                onItemClickEvent(adapterPosition)
            }
        }
    }
}
//endregion

//region=============Time Adapter:-
class TimeAdapter(private var timeList : MutableList<Session>?,
                        private var onItemClickEvent: (Int) -> Unit,
                        private val mContext: Context
) :
    RecyclerView.Adapter<TimeAdapter.TimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        return TimeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.time_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int = timeList?.size ?: 0

    inner class TimeViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val container = itemView.findViewById<ConstraintLayout>(R.id.container)
        val timeTV = itemView.findViewById<TextView>(R.id.timeTV)

        fun bindData(position: Int){
            val time = timeList?.get(position)?.Showtime
            if(!time.isNullOrEmpty()){
                timeTV.text = time
            }
        }

        init {
            container.setOnClickListener {
                onItemClickEvent(adapterPosition)
            }
        }
    }
}
//endregion