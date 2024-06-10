package com.example.playlistmaker.media.model.data.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.media.model.repository.StorageRepository
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

const val PLAYLIST_PREFIX = "PL"
const val IMAGE_FILE_EXTENSION = "jpg"

class PrivateStorageImageRepositoryImpl(private val context: Context) : StorageRepository {
    override fun saveImageByUri(imageUri: Uri, fileName: String):Uri {
        if (imageUri == Uri.EMPTY) return  Uri.EMPTY
        val filePath =
            File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                context.packageName
            )
        if (!filePath.exists()) filePath.mkdirs()
        val file = File(filePath, "$PLAYLIST_PREFIX$fileName.$IMAGE_FILE_EXTENSION")

        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
        val outputStream: OutputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.toUri()
    }
}