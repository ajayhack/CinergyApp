package com.example.cinergyapp.modal.webservice

import com.example.cinergyapp.modal.EscapeRoomMoviesRequest
import com.example.cinergyapp.modal.EscapeRoomMoviesResponse
import com.example.cinergyapp.modal.GuestLoginRequest
import com.example.cinergyapp.modal.GuestLoginResponse
import com.example.cinergyapp.modal.GuestTokenRequest
import com.example.cinergyapp.modal.GuestTokenResponse
import com.example.cinergyapp.modal.MovieInfoResponse
import com.example.cinergyapp.modal.MoviesInfoRequest
import com.example.cinergyapp.utils.AppPreference
import com.example.cinergyapp.utils.GUEST_TOKEN
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

const val BASE_URL = "https://cinergyapp.thetunagroup.com/api/"
const val GuestToken = "guestToken"
const val Login = "login"
const val ESCAPE_ROOM_MOVIES = "escapeRoomMovies"
const val MOVIE_INFO = "getMovieInfo"

class WebService {
   private lateinit var apiInterface: APIInterface

   init {
       val retrofit = Retrofit.Builder()
           .baseUrl(BASE_URL)
           .addConverterFactory(GsonConverterFactory.create())
           .build()
       apiInterface = retrofit.create(APIInterface::class.java)
   }

    suspend fun getGuestToken(guestTokenRequest: GuestTokenRequest) : GuestTokenResponse {
        return apiInterface.guestToken(guestTokenRequest)
    }

    suspend fun getGuestLogin(token: String , guestLoginRequest: GuestLoginRequest) : GuestLoginResponse {
        return apiInterface.guestLogin("Bearer $token" , guestLoginRequest)
    }

    suspend fun getEscapeMovies(token: String , userid: String? ,  escapeRoomMoviesRequest: EscapeRoomMoviesRequest) : EscapeRoomMoviesResponse {
        return apiInterface.getEscapeRoomMovies("Bearer $token" , userid , escapeRoomMoviesRequest)
    }

    suspend fun getMovieInfo(token: String , userid: String? ,  moviesInfoRequest: MoviesInfoRequest) : MovieInfoResponse {
        return apiInterface.getMovieInfo("Bearer $token" , userid , moviesInfoRequest)
    }

    internal interface APIInterface {
        @Headers("Content-Type: application/json", "Accept: application/json")
        @POST(GuestToken)
        suspend fun guestToken(@Body guestTokenRequest: GuestTokenRequest): GuestTokenResponse

        @Headers("Content-Type: application/json", "Accept: application/json")
        @POST(Login)
        suspend fun guestLogin(@Header("Authorization") token: String , @Body guestLoginRequest: GuestLoginRequest): GuestLoginResponse

        @Headers("Content-Type: application/json", "Accept: application/json")
        @POST(ESCAPE_ROOM_MOVIES)
        suspend fun getEscapeRoomMovies(@Header("Authorization") token: String , @Header("userid") userid : String?  ,  @Body escapeRoomMoviesRequest: EscapeRoomMoviesRequest): EscapeRoomMoviesResponse

        @Headers("Content-Type: application/json", "Accept: application/json")
        @POST(MOVIE_INFO)
        suspend fun getMovieInfo(@Header("Authorization") token: String , @Header("userid") userid : String?  ,  @Body moviesInfoRequest: MoviesInfoRequest): MovieInfoResponse
    }
}