package com.tlilektik.oselottest.service

import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    @GET("rrafols/mobile_test/master/data.json")
    fun getList(): Call<Response>
}