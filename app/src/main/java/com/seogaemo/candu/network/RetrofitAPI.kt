package com.seogaemo.candu.network

import com.seogaemo.candu.data.CompleteRequest
import com.seogaemo.candu.data.CompleteResponse
import com.seogaemo.candu.data.ContentRequest
import com.seogaemo.candu.data.ContentResponse
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

    @POST("gpt/chapter/content")
    suspend fun getContent(
        @Body body: ContentRequest
    ): Response<ContentResponse>

    @POST("gpt/chapter/complete")
    suspend fun getImage(
        @Body body: CompleteRequest
    ): Response<CompleteResponse>
}