package com.seogaemo.candu.data

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Goal (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val item: GoalResponse,
    val color: Int
)