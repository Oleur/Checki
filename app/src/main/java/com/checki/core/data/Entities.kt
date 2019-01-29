package com.checki.core.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class NetService(
    @PrimaryKey @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "url") var url: String,
    @ColumnInfo(name = "status") var status: Int = 0,
    @ColumnInfo(name = "created_at") var createdAt: Long,
    @ColumnInfo(name = "last_checked_at") var lastCheckedAt: Long
) : Parcelable