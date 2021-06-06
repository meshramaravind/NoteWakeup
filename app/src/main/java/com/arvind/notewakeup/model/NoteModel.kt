package com.arvind.notewakeup.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

@Entity(tableName = "NoteWakeUp")
@Parcelize
data class NoteModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val noteTitle: String,
    val noteBody: String,
    val notedate: String,
    val createdAt: Long = System.currentTimeMillis(),
) : Parcelable {
    val createdAtDateFormat: String
        get() = DateFormat.getDateInstance()
            .format(createdAt) // Date Format: Jan 11, 2021, 11:30 AM
}
