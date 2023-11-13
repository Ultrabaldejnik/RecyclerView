package com.example.recyclerview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID


@Parcelize
data class Contact(
    val id : Int,
    val firstName : String,
    val lastName : String,
    val phoneNumber : String,
    val enabled : Boolean = false
) : Parcelable {
    companion object{
        const val UNDEFINED_ID = 0
    }
}