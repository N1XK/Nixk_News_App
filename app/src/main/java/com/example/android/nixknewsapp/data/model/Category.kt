package com.example.android.nixknewsapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val imageId: Int,
    val title: String,
): Parcelable