package com.seogaemo.candu.data

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Goal (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val item: GoalResponse,
    val color: Int,
    var level: Int
): Parcelable