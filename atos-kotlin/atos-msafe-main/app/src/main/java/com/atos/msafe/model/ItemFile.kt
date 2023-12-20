package com.atos.msafe.model

import com.google.firebase.storage.StorageReference

data class ItemFile(
    val name: String,
    val updatedTimeMillis: String,
    val fileSize: String,
    val fileType: String,
    val imagePlaceholder: String?,
    val storageReference: StorageReference,
    val indexFireStore: Int,
    val favourite: Boolean
){
    fun getThumbnailFromUrl() = "$imagePlaceholder"
}
