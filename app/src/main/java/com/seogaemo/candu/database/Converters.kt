package com.seogaemo.candu.database
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.seogaemo.candu.data.GoalResponse


object Converters {
    @TypeConverter
    fun fromString(value: String?): GoalResponse {
        val type = object : TypeToken<GoalResponse?>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromGoalResponse(goalResponse: GoalResponse?): String {
        val gson = Gson()
        return gson.toJson(goalResponse)
    }
}
