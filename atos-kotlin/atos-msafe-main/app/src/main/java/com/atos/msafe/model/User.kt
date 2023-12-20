package com.atos.msafe.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val emailAddress: String = "",
    val firstName: String? = null,
    val lastName: String? = null,
    val profilePicture: String? = null,
    val accountType: Int = 0,
    val verified: Boolean = false,
    val files: List<File>? = null,
    var pin: String? = null
): Parcelable

@Parcelize
data class File(
    val name: String = "",
    val extension: String = "",
    var hidden: Boolean = false,
    val thumbnail: Boolean = false,
    var favourite: Boolean = false
) : Parcelable