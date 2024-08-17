package com.seogaemo.candu.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Story(
    val content: String,
    val title: String
): Parcelable