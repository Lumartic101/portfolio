package com.atos.msafe.shared

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.Toast
import androidx.annotation.StringRes
import com.atos.msafe.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


@SuppressLint("ResourceType")
fun Button.toggleButton(enable: Boolean = false) {
    this.setBackgroundColor(if (enable) resources.getInteger(R.color.purple_700) else resources.getInteger(
        R.color.purple_200
    ))
    this.alpha = if(enable) 1.0f else 0.4f
    this.isEnabled = enable
}

fun Activity.toast(@StringRes resource: Int, lengthType: Int) {
    Toast.makeText(this, getString(resource),
        lengthType).show()
}

fun Context.getFileName(uri: Uri): String? = when(uri.scheme) {
    ContentResolver.SCHEME_CONTENT -> getContentFileName(uri)
    else -> uri.path?.let(::File)?.name
}

private fun Context.getContentFileName(uri: Uri): String? = runCatching {
    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        cursor.moveToFirst()
        return@use cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME).let(cursor::getString)
    }
}.getOrNull()

fun Activity.makeCachedFileFromUri(uri: Uri): File {
    val receivedFile = File(getFileName(uri).toString())
    val parcelFileDescriptor = this.contentResolver?.openFileDescriptor(uri, "r", null)
    val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
    val file = File(this.cacheDir, receivedFile.name)
    inputStream.copyTo(FileOutputStream(file))
    return file
}