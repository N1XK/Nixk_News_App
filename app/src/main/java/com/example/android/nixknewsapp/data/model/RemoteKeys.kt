package com.example.android.nixknewsapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val uuid: String,
    val prevKey: Int?,
    val nextKey: Int?
)