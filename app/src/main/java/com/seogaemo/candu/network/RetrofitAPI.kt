package com.seogaemo.candu.network

import com.seogaemo.candu.data.GoalRequest
import com.seogaemo.candu.data.GoalResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface RetrofitAPI {
    @POST("gpt/goal/new")
    suspend fun goalNew(
        @Body body: GoalRequest
    ): Response<GoalResponse>
}