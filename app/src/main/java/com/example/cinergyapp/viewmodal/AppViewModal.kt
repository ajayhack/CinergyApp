package com.example.cinergyapp.viewmodal

import androidx.lifecycle.ViewModel
import com.example.cinergyapp.modal.DataRepository
import com.example.cinergyapp.modal.EscapeRoomMoviesRequest
import com.example.cinergyapp.modal.EscapeRoomMoviesResponse
import com.example.cinergyapp.modal.GuestLoginRequest
import com.example.cinergyapp.modal.GuestLoginResponse
import com.example.cinergyapp.modal.GuestTokenRequest
import com.example.cinergyapp.modal.GuestTokenResponse
import com.example.cinergyapp.modal.MovieInfoResponse
import com.example.cinergyapp.modal.MoviesInfoRequest

class AppViewModal(private val dataRepository: DataRepository = DataRepository()) : ViewModel() {

    public suspend fun getGuestTokenData(guestTokenRequest: GuestTokenRequest) : GuestTokenResponse{
        return dataRepository.getGuestTokenData(guestTokenRequest)
    }

    public suspend fun getGuestLoginData(token : String , guestLoginRequest: GuestLoginRequest) : GuestLoginResponse{
        return dataRepository.getGuestLoginData(token , guestLoginRequest)
    }

    public suspend fun getEscapeMoviesData(token : String , userId : String , escapeRoomMoviesRequest: EscapeRoomMoviesRequest) : EscapeRoomMoviesResponse{
        return dataRepository.getEscapeMoviesData(token , userId , escapeRoomMoviesRequest)
    }

    public suspend fun getMovieInfoData(token : String , userId : String , moviesInfoRequest: MoviesInfoRequest) : MovieInfoResponse{
        return dataRepository.getMovieInfoData(token , userId , moviesInfoRequest)
    }
}