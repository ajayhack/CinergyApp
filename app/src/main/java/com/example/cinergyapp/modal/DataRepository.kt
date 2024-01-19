package com.example.cinergyapp.modal

import com.example.cinergyapp.modal.webservice.WebService

class DataRepository(private var webService: WebService = WebService()) {
    suspend fun getGuestTokenData(guestTokenRequest: GuestTokenRequest) : GuestTokenResponse {
        return webService.getGuestToken(guestTokenRequest)
    }

    suspend fun getGuestLoginData(token : String , guestLoginRequest: GuestLoginRequest) : GuestLoginResponse {
        return webService.getGuestLogin(token , guestLoginRequest)
    }
    suspend fun getEscapeMoviesData(token : String , userId : String , escapeRoomMoviesRequest: EscapeRoomMoviesRequest) : EscapeRoomMoviesResponse {
        return webService.getEscapeMovies(token , userId , escapeRoomMoviesRequest)
    }
    suspend fun getMovieInfoData(token : String , userId : String , moviesInfoRequest: MoviesInfoRequest) : MovieInfoResponse {
        return webService.getMovieInfo(token , userId , moviesInfoRequest)
    }
}