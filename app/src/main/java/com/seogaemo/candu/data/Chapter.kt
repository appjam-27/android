package com.seogaemo.candu.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Chapter(
    val desc: String,
    val duration: String,
    val title: String
): Parcelable