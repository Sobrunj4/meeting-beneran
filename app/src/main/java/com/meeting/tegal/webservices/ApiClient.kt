package com.meeting.tegal

import com.example.meeting.models.Food
import com.example.meeting.models.MeetingRoom
import com.example.meeting.models.User
import com.example.meeting.utilities.Constants
import com.example.meeting.utilities.WrappedListResponse
import com.example.meeting.utilities.WrappedResponse
import com.meeting.tegal.models.CreateOrder
import com.meeting.tegal.models.Order
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private var retrofit: Retrofit? = null

        private val opt = OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
        }.build()

        private fun getClient(): Retrofit {
            return if (retrofit == null) {
                retrofit = Retrofit.Builder().apply {
                    client(opt)
                    baseUrl(Constants.END_POINT)
                    addConverterFactory(GsonConverterFactory.create())
                }.build()
                retrofit!!
            } else {
                retrofit!!
            }
        }

        fun instance() = getClient().create(ApiService::class.java)
    }
}

interface ApiService{

    @FormUrlEncoded
    @POST("user/login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String
    ) : Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("user/register")
    fun register(
        @Field("nama") nama : String,
        @Field("email") email : String,
        @Field("password") password : String,
        @Field("no_hp") no_hp : String,
        @Field("uname") uname : String
    ) : Call<WrappedResponse<User>>

    @GET("partner")
    fun fetchPartners() : Call<WrappedListResponse<Partner>>

    @FormUrlEncoded
    @POST("partner/search")
    fun searchPartners(
        @Field("date") date : String,
        @Field("start_time") startTime : String,
        @Field("end_time") endTime : String
    ) : Call<WrappedListResponse<Partner>>

    @GET("room/promo")
    fun fetchRoomsPromo() : Call<WrappedListResponse<MeetingRoom>>

    @GET("room/{id_partner}")
    fun fetchRoomsByPartner(
        @Path("id_partner") idPartner : Int
    ) : Call<WrappedListResponse<MeetingRoom>>

    @GET("food/{id_partner}")
    fun fetchFoodsByPartner(
        @Path("id_partner") idPartner : Int
    ) : Call<WrappedListResponse<Food>>


    @Headers("Content-Type: application/json")
    @POST("order")
    fun order(
        @Header("Authorization") token : String,
        @Body body : RequestBody
    ) : Call<WrappedResponse<CreateOrder>>

    @GET("order/{id}/cancel")
    fun orderCancel(
        @Header("Authorization") token : String,
        @Path("id") orderId : Int
    ) : Call<WrappedResponse<Order>>

    @GET("order/user")
    fun getOrderByUser(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Order>>













    @GET("ruangmeeting")
    fun getMeetingRooms(@Header("Authorization") token : String) : Call<WrappedListResponse<MeetingRoom>>

    @FormUrlEncoded
    @POST("ruangmeeting/search")
    fun search(
        @Header("Authorization") token : String,
        @Field("tanggal_dan_waktu") tanggalDanWaktu : String,
        @Field("lama") lama : String
    ) : Call<WrappedListResponse<MeetingRoom>>

    @GET("user/profile")
    fun profile(
        @Header("Authorization") token : String
    ) : Call<WrappedResponse<User>>

    @GET("makanan/{id_mitra}")
    fun getMakanan(
        @Header("Authorization") token : String,
        @Path("id_mitra") id_mitra : Int
    ) : Call<WrappedListResponse<Food>>

    @FormUrlEncoded
    @POST("order/{id}/update")
    fun updateStatus(
        @Header("Authorization") token : String,
        @Path("id") id : Int,
        @Field("status") status : String
    ): Call<WrappedResponse<Order>>
}