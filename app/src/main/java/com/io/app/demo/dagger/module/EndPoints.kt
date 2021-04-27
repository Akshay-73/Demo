package com.io.app.demo.dagger.module

import com.io.app.demo.response.RestaurantResponse
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface EndPoints {

    @Streaming
    @GET
    fun downloadFile(@Url fileUrl: String?): Observable<Response<ResponseBody>>

}