package com.atos.msafe.repository

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.Intent.createChooser
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ProgressBar
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.atos.msafe.BuildConfig
import com.atos.msafe.R
import com.atos.msafe.model.File
import com.atos.msafe.model.ItemFile
import com.atos.msafe.model.NewUser
import com.atos.msafe.model.User
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.internal.StorageReferenceUri
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.text.DateFormat
import java.util.*

const val nameCollection = "Users"
const val fieldVerified = "verified"
const val fieldFirstName = "firstName"
const val fieldPin = "pin"
const val fieldLastName = "lastName"
const val fieldFiles = "files"
const val fieldFolderThumb = "THUMB"
const val nameExtensionThumb = "_THUMB"
const val fieldFolderThumbPp = "profilePicture"
const val nameExtensionThumbPp = "_profilePicture"

@Suppress("DEPRECATION")
class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _result: MutableLiveData<Boolean> = MutableLiveData()
    private val _resetMailSend: MutableLiveData<Boolean> = MutableLiveData()
    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    private val _accountUser: MutableLiveData<User> = MutableLiveData()
    private val _dataListViewer: MutableLiveData<List<ItemFile>> = MutableLiveData()

    val result: LiveData<Boolean>
        get() = _result

    val errorMessage: LiveData<String>
        get() = _errorMessage

    val resetMailSend: LiveData<Boolean>
        get() = _resetMailSend

    val accountUser: LiveData<User>
        get() = _accountUser

    val dataListViewer: LiveData<List<ItemFile>>
        get() = _dataListViewer

    /**
     * @brief                   Create a new account based on the information in the object
     * @param newUser           Object of [NewUser] containing all the information
     */
    suspend fun createUser(newUser: NewUser) {
        withTimeout(5_000) {
            auth.createUserWithEmailAndPassword(newUser.emailAddress, newUser.password)
                .addOnCompleteListener(Activity()) {
                    if (it.isSuccessful) {
                        firestore.collection(nameCollection)
                            .document(auth.currentUser?.uid.toString())
                            .set(
                                User(
                                    emailAddress = newUser.emailAddress,
                                    accountType = 0,
                                    verified = false
                                )
                            )
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    auth.currentUser?.sendEmailVerification()
                                        ?.addOnCompleteListener {
                                            _result.value = task.isSuccessful
                                        }
                                }
                            }
                    } else {
                        _errorMessage.value = it.exception?.message
                    }
                }
        }
    }

    /**
     * @brief                   Sign in the user with the given email address and password
     * @param emailAddress      Given email address
     * @param password          Given password
     */
    suspend fun logUserInWithEmailGetUser(emailAddress: String, password: String) {
        withTimeout(5_000) {
            auth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    getUserFromDatabase()
                } else {
                    _errorMessage.value = it.exception?.message
                }
            }
        }
    }

    /**
     * @brief                   Acquire lasted collection of the current user
     */
    fun getUserFromDatabase() {
        firestore.collection(nameCollection).document(auth.currentUser?.uid.toString()).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    if (result.exists()) {
                        _accountUser.value = result.toObject(User::class.java)
                        _result.value = task.isSuccessful
                    }
                } else {
                    _errorMessage.value = task.exception?.message
                }
            }
    }


    /**
     * @brief                   Update the name of the user in firebase
     * @param firstName         New first name
     * @param lastName          New last name
     */
     fun updateNameUser(firstName: String, lastName: String) {
         firestore.collection("Users").document(auth.currentUser?.uid.toString())
             .update(fieldFirstName, firstName, fieldLastName, lastName )
    }

    /**
     * @brief                   Update the pin of the user in firebase
     * @param pinCode           set pincode
     */
    fun setPinCode(pinCode: String?) {
        firestore.collection("Users").document(auth.currentUser?.uid.toString())
            .update(fieldPin, pinCode)
    }

    /**
     * @brief                   Sign current user out
     */
    fun signOut() {
        auth.signOut()
    }

    /**
     * @brief                   Set all the data in the repo back to 'null'
     */
    fun resetData() {
        _result.value = null
        _errorMessage.value = null
        _accountUser.value = null
    }

    /**
     * @brief                   Set index 'Verified' to true in the firebase
     */
    suspend fun setVerifiedEmail() {
        withTimeout(5_000) {
            firestore.collection(nameCollection).document(auth.currentUser?.uid.toString())
                .update(fieldVerified, true)
                .addOnCompleteListener { task ->
                    _result.value = task.isSuccessful
                    if (!task.isSuccessful) {
                        _errorMessage.value = task.exception?.message
                    }
                }
        }
    }

    /**
     * @brief                   Send the user a email to reset his password
     * @param emailAddress      Address to send it too
     */
    suspend fun sendResetPasswordLink(emailAddress: String) {
        withTimeout(5_000) {
            auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener { task ->
                _resetMailSend.value = task.isSuccessful
                if (!task.isSuccessful) {
                    _errorMessage.value = task.exception?.message
                }
            }
        }
    }


    /**
     * @brief                   Upload a picture to the cloud and update the firebase accordingly
     * @param contentResolver   The view where to bind the snackBar too
     * @param fileExtension     Extension type of the file
     * @param fileUri           Uri to the file
     */
    suspend fun updateProfilePic(
        contentResolver: ContentResolver,
        fileExtension: String,
        fileUri: Uri
    ) {
        val storageRefer = Firebase.storage.reference

        withTimeout(5_000) {
            // Create a new bitmap
            withContext(Dispatchers.IO) {
                val bitmap: Bitmap? = runCatching {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            contentResolver,
                            fileUri
                        )
                    )
                }.getOrNull()

                val baos = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 15, baos)
                val data = baos.toByteArray()
                // Then make a new path in the file
                val profilePicRef = auth.currentUser?.uid?.let {
                    storageRefer.child(
                        it
                    ).child(fieldFolderThumbPp)
                        .child("$nameExtensionThumbPp.$fileExtension")
                }
                // And upload to the cloud
                profilePicRef?.putBytes(data)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        firestore.collection("Users").document(auth.currentUser?.uid.toString())
                            .update(fieldFolderThumbPp, profilePicRef.toString())
                    }
                }
            }
        }
    }


    /**
     * @brief                   Upload a file to the cloud and update the firebase accordingly
     * @param contentResolver   The view where to bind the snackBar too
     * @param fileName          Name of the file
     * @param fileExtension     Extension type of the file
     * @param fileUri           Uri to the file
     * @param hidden            File need to hidden?
     * @param view              The view where to bind the snackBar too
     * @param context           Context where it takes place
     */
    suspend fun uploadFileToCloudAddToUser(
        contentResolver: ContentResolver,
        fileName: String,
        fileExtension: String,
        fileUri: Uri,
        hidden: Boolean,
        view: View?,
        context: Context?
    ) {
        val storageRef = Firebase.storage.reference
        var userRef = auth.currentUser?.uid?.let { uid -> storageRef.child(uid).child(fileName).child("$fileName$fileExtension")}
        val snackBar = view?.let { showSnackBar(context!!.getString(R.string.txt_toast_file_uploading), it, context) }
        var thumbnail = false
        withTimeout(5_00) {
            userRef?.putFile(fileUri)?.addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    // If a thumbnail is supported, make a compressed version
                    val fileExtensionLowerCase = fileExtension.lowercase(Locale.getDefault())
                    if (fileExtensionLowerCase == ".png" || fileExtensionLowerCase == ".jpg" || fileExtensionLowerCase == ".`jpeg") {
                        // Create a new bitmap
                        val bitmap = ImageDecoder.decodeBitmap(
                            ImageDecoder.createSource(
                                contentResolver,
                                fileUri
                            )
                        )
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 15, baos)
                        val data = baos.toByteArray()
                        // Then make a new path in the file
                        userRef = auth.currentUser?.uid?.let {
                            storageRef.child(
                                it
                            ).child(fileName).child(fieldFolderThumb)
                                .child(fileName + nameExtensionThumb + fileExtensionLowerCase)
                        }
                        thumbnail = true
                        // And upload to the cloud
                        userRef?.putBytes(data)
                    }
                    firestore.collection(nameCollection)
                        .document(auth.currentUser?.uid.toString())
                        .update(
                            fieldFiles,
                            FieldValue.arrayUnion(
                                File(
                                    name = fileName,
                                    extension = fileExtension,
                                    hidden = hidden,
                                    thumbnail = thumbnail,
                                    favourite = false
                                )
                            )
                        ).addOnCompleteListener { task ->
                            _result.value = task.isSuccessful
                            _errorMessage.value = task.exception?.message
                            snackBar?.dismiss()
                            getUserFromDatabase()
                        }
                } else {
                    _errorMessage.value = it.exception?.message
                }
            }
        }

    }
    /**
     * @brief             Show a snackBar when uploading a file
     * @param view        The view where to bind the snackBar too
     * @param context     Context where it takes place
     * @return bar        The object is returned
     */
    private fun showSnackBar(text: String, view: View, context: Context?): Snackbar {
        val bar: Snackbar = Snackbar.make(view, text, Snackbar.LENGTH_INDEFINITE)
        val contentLay =
            bar.view.findViewById<View>(com.google.android.material.R.id.snackbar_text).parent as ViewGroup
        val item = ProgressBar(context)
        contentLay.addView(item, 0)
        bar.show()
        return bar
    }

    /**
     * @brief           Get a list of the files, this list includes all the metadata and information about the file.
     * @param files     The list of files the user holds in his account, derived of [User.files]
     * @param hidden    False for not hidden files, true for hidden files
     * @param context   Used to format the file size
     */
    suspend fun getDataForListViewer(files: List<File>, hidden: Boolean, context: Context) {
        val storageRef = Firebase.storage.reference
        var fileRef: StorageReference
        val list = arrayListOf<ItemFile>()
        files.forEachIndexed { index ,file ->
            try {
                withTimeout(5_000) {
                    if (file.hidden == hidden) {
                        fileRef =
                            storageRef.child(auth.currentUser?.uid.toString()).child(file.name)
                                .child(file.name + file.extension)
                        val result = fileRef.metadata.await()
                        val itemToAdd =
                            ItemFile(
                            name = result.name.toString(),
                            updatedTimeMillis = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).format(Date(result.updatedTimeMillis)),
                            fileSize = android.text.format.Formatter.formatShortFileSize(context, result.sizeBytes),
                            fileType = file.extension,
                            imagePlaceholder = if (file.thumbnail) {
                                downloadThumbnail(
                                    storageRef.child(auth.currentUser?.uid.toString())
                                        .child(file.name).child(fieldFolderThumb).child(file.name + nameExtensionThumb + file.extension)
                                        .toString())
                            } else {
                                null
                            },
                            storageReference = storageRef.child(auth.currentUser?.uid.toString())
                                .child(file.name).child(file.name + file.extension),
                            indexFireStore = index,
                            favourite= file.favourite
                        )
                        if (itemToAdd.favourite){
                            list.add(0,itemToAdd)
                        } else {
                            list.add(itemToAdd)
                        }
                    }
                }

            } catch (e: Exception) {
                println(e)
            }
        }
        _dataListViewer.value = list
    }

    /**
     * @brief                If a thumbnail is present, we download the file and return the URI to the file.
     * @param storageRef     The Ref to the file in the cloud storage
     * @return result        Return the Uri to the file
     */
    suspend fun downloadThumbnail(storageRef: String): String {
        val gsRef = Firebase.storage.getReferenceFromUrl(storageRef)
        val result: String
        withTimeout(1000){
            val uri = gsRef.downloadUrl.await().toString()
            result = uri
        }
        return result
    }

    /**
     * @brief                Delete a file from your storage based on a item in the recyclerview
     * @param itemFile       item in recyclerview to delete
     */
    fun deleteFile(itemFile: ItemFile) {
        val storageRef = Firebase.storage.reference
        val fileNameWithoutExtension = itemFile.name.substring(0, itemFile.name.lastIndexOf('.'))
        var fileRef = auth.currentUser?.uid?.let { uid -> storageRef.child(uid)
            .child(fileNameWithoutExtension)
            .child(itemFile.name)}

        fileRef?.delete()

        if (_accountUser.value?.files?.get(itemFile.indexFireStore)?.thumbnail == true){
            fileRef = auth.currentUser?.uid?.let { uid -> storageRef.child(uid)
                .child(fileNameWithoutExtension)
                .child(fieldFolderThumb)
                .child(fileNameWithoutExtension + nameExtensionThumb + itemFile.fileType)
            }
            fileRef?.delete()
        }

        firestore.collection(nameCollection)
            .document(auth.currentUser?.uid.toString())
            .update(
                fieldFiles,
                FieldValue.arrayRemove(_accountUser.value?.files?.get(itemFile.indexFireStore))
            )

        getUserFromDatabase()
    }

    /**
     * @brief                Return the index of a itemFile located in the [List<File>?]
     * @param fileName       item in recyclerview to get index of in the fire
     * @return Int           Index of the file, -1 if not found
     */
    @Deprecated ("Not used anymore, itemFile has index already in the object, see ItemFile.indexFireStore")
    private fun findIndexCollectionFile(fileName: String): Int {
        _accountUser.value?.files?.forEachIndexed { index, file ->
            if (file.name == fileName) return index
        }
        return -1
    }

    /**
     * @brief                Download selected file to the cashed folder of the app
     * @param itemFile       item in recyclerview to download
     * @param view           Used for the SnackBar to bind to
     * @param context        Used by the SnackBar
     */
    suspend fun downloadFileToCash(itemFile: ItemFile, view: View?, context: Context?) {
        val snackBar = view?.let { showSnackBar(context!!.getString(R.string.txt_toast_file_downloading), it, context) }
        try {
            withContext(Dispatchers.IO) {
                val fileTemp: java.io.File? = runCatching {
                    createTempFile(itemFile.name, itemFile.fileType)
                }.getOrNull()

                withTimeout(7_500) {
                    if (fileTemp != null) {
                        itemFile.storageReference.getFile(fileTemp).addOnCompleteListener{
                            if (it.isSuccessful) {
                                snackBar?.dismiss()
                                if (context != null) {
                                    val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileTemp.extension)
                                    val content = FileProvider.getUriForFile(
                                        Objects.requireNonNull(context),
                                        BuildConfig.APPLICATION_ID + ".provider", fileTemp
                                    )
                                    val intent = Intent()
                                    intent.action = Intent.ACTION_VIEW
                                    intent.setDataAndType(content, mime)
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    startActivity(context, intent, null)
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            throw FirebaseExceptionError("Unable to download file to cash: ${e.localizedMessage}")
        }
    }

    /**
     * @brief                Toggle the "hidden" Boolean in the FireStore Array for a selected file
     * @param itemFile       item where to toggle "hidden"
     */
    suspend fun moveFile(itemFile: ItemFile) {
        _accountUser.value?.files?.get(itemFile.indexFireStore)?.hidden = _accountUser.value?.files?.get(itemFile.indexFireStore)?.hidden == false
        try {
            withTimeout(5_000) {
                firestore.collection(nameCollection).document(auth.currentUser?.uid.toString())
                    .update(
                        fieldFiles, _accountUser.value?.files
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            getUserFromDatabase()
                        }
                    }
            }
        } catch (e: Exception) {
            throw FirebaseExceptionError("Unable to move file: $e")
        }
    }

    /**
     * @brief                Download the file to the Download folder of the phone
     * @param itemFile       item that the users want to download
     * @param view           Used for the SnackBar to bind to
     * @param context        Used by the SnackBar
     */
    suspend fun downloadFileToDownloadFolder(itemFile: ItemFile, view: View?, context: Context?) {
        val snackBar = view?.let { showSnackBar(context!!.getString(R.string.txt_toast_file_downloading), it, context) }
        try {
            withContext(Dispatchers.IO) {
                val fileTemp: java.io.File? = runCatching {
                    createTempFile(itemFile.name, itemFile.fileType)
                }.getOrNull()
                withTimeout(7_500) {
                    if (fileTemp != null) {
                        itemFile.storageReference.getFile(fileTemp).addOnCompleteListener{
                            if (it.isSuccessful) {
                                if (context != null) {
                                    copyFileToDownloads(context,fileTemp)
                                }
                                fileTemp.delete()
                                snackBar?.dismiss()
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            throw FirebaseExceptionError("Unable to download file to cash: ${e.localizedMessage}")
        }
    }

    /**
     * @brief                After downloading the file to the cashed dir, move it to the Download folder of the phone
     * @param context        Needed for the contentResolver
     * @param downloadedFile File that is downloaded and needs to move
     * @return Uri           Uri to the file in the download folder
     */
    private fun copyFileToDownloads(context: Context, downloadedFile: java.io.File): Uri? {
        val downloadDirection = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        val resolver = context.contentResolver
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, downloadedFile.name)
                put(MediaStore.MediaColumns.MIME_TYPE, downloadedFile.extension)
                put(MediaStore.MediaColumns.SIZE, downloadedFile.totalSpace)
            }
            resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        } else {
            val authority = "${context.packageName}.provider"
            val destinyFile = java.io.File(downloadDirection, downloadedFile.name)
            FileProvider.getUriForFile(context, authority, destinyFile)
        }?.also { downloadedUri ->
            resolver.openOutputStream(downloadedUri).use { outputStream ->
                val brr = ByteArray(1024)
                var len: Int
                val bufferedInputStream = BufferedInputStream(FileInputStream(downloadedFile.absoluteFile))
                while ((bufferedInputStream.read(brr, 0, brr.size).also { len = it }) != -1) {
                    outputStream?.write(brr, 0, len)
                }
                outputStream?.flush()
                bufferedInputStream.close()
            }
        }
    }

    /**
     * @brief                Download selected file and then open the ShareSheet so that the user can share the file
     * @param itemFile       item that the users want to share
     * @param view           Used for the SnackBar to bind to
     * @param context        Used by the SnackBar
     */
    suspend fun shareFileToOther(itemFile: ItemFile, view: View?, context: Context?) {
        val snackBar = view?.let { showSnackBar(context!!.getString(R.string.txt_toast_file_downloading), it, context) }
        try {
            withContext(Dispatchers.IO) {
                val fileTemp: java.io.File? = runCatching {
                    createTempFile(itemFile.name, itemFile.fileType)
                }.getOrNull()
                withTimeout(7_500) {
                    if (fileTemp != null) {
                        itemFile.storageReference.getFile(fileTemp).addOnCompleteListener {
                            if (it.isSuccessful) {
                                if (context != null) {
                                    snackBar?.dismiss()
                                    val content = FileProvider.getUriForFile(
                                        Objects.requireNonNull(context),
                                        BuildConfig.APPLICATION_ID + ".provider", fileTemp
                                    )
                                    val intent = Intent()
                                    intent.action = Intent.ACTION_SEND
                                    intent.type = "application/octet-stream"
                                    intent.putExtra(Intent.EXTRA_STREAM, content)
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    startActivity(context, createChooser(intent, "Share your file"), null)
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            throw FirebaseExceptionError("Unable to download file to cash: ${e.localizedMessage}")
        }
    }

    suspend fun setFavourite(itemFile: ItemFile) {
        _accountUser.value?.files?.get(itemFile.indexFireStore)?.favourite = _accountUser.value?.files?.get(itemFile.indexFireStore)?.favourite == false
        try {
            withTimeout(5_000) {
                firestore.collection(nameCollection).document(auth.currentUser?.uid.toString())
                    .update(
                        fieldFiles, _accountUser.value?.files
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            getUserFromDatabase()
                        }
                    }
            }
        } catch (e: Exception) {
            throw FirebaseExceptionError("Unable to set file as favourite: $e")
        }
    }

    inner class FirebaseExceptionError(message: String) : Exception(message)
}
