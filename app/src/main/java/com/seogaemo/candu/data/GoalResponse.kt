package com.seogaemo.candu.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GoalResponse(
    val chapters: List<Chapter>,
    val goal: String,
    val icon: String,
    val story: Story
): Parcelable