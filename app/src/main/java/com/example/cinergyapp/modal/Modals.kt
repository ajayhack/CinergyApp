package com.example.cinergyapp.modal

data class GuestTokenRequest(var secret_key : String? = null ,
                             var device_type : String? = null , var device_id : String? = null , var push_token : String? = "")
data class GuestTokenResponse(var token : String? = null , var response : String? = null , var message : String? = null)

data class GuestLoginRequest(var device_type : String? = null, var device_id : String? = null, var login_type : String? = null)
data class GuestLoginResponse(var user : User? = null , var response : String? = null , var message : String? = null)
data class User(var id : String? = null , var role_id : String? = null , var name : String? = null , var username : String? = null ,
                var member_id : String? = null)
data class EscapeRoomMoviesRequest(var device_id : String? = null , var member_id : String? = null , var device_type : String? = null ,
                                   var location_id : String? = null)

data class EscapeRoomMoviesResponse(var device_id : String? = null , var er_tickets : String? = null , var escape_rooms_movies : MutableList<RoomData>? = null ,
                                   var location_id : String? = null , var response : String? = null , var message : String? = null)
data class RoomData(var ID : String? = null , var ScheduledFilmId : String? = null , var slug : String? = null , var Title : String? = null ,
                    var Rating : String? = null , var RunTime : String? = null , var Synopsis : String? = null , var image_url : String? = null , var image_url_poster : String? = null)

data class MoviesInfoRequest(var device_id : String? = null , var device_type : String? = null ,
                                   var location_id : String? = null , var movie_id : String? = null)
data class MovieInfoResponse(var movie_info : MovieInfo? = null , var er_tickets : String? = null , var escape_rooms_movies : MutableList<RoomData>? = null ,
                                    var location_id : String? = null , var response : String? = null , var message : String? = null)
data class ChooseDateModal(var date : String? = null , var isSelected : Boolean? = false)
data class MovieInfo(var ScheduledFilmId : String? = null , var slug : String? = null , var Title : String? = null ,
                     var Rating : String? = null , var RunTime : String? = null , var Synopsis : String? = null , var image_url : String? = null ,
                     var image_url_poster : String? = null , var Genres : String? = null , var date_list : MutableList<String>? = null ,
                     var show_times : MutableList<ShowTime>? = null)
data class ShowTime(var date : String? = null , var sessions : MutableList<Session>? = null)
data class Session(var ScheduledFilmId : String? = null , var Showtime : String? = null)