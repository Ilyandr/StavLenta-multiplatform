package gcu.production.stavlenta.android.repository.features.utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.provider.MediaStore
import android.util.Base64
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream


fun Context.requireGifImageLoader() = ImageLoader.Builder(this).components {
    if (SDK_INT >= 28) add(ImageDecoderDecoder.Factory())
    else add(GifDecoder.Factory())
}.build()

fun Pair<String?, String?>.toBase64(): String =
    "Basic ${Base64.encodeToString(("$first:$second").toByteArray(), Base64.NO_WRAP)}"

internal object FlowSupport {

    infix fun <T : Any?> MutableStateFlow<T>.set(newState: T) =
        apply {
            value = newState
        }
}

internal fun isNetworkAvailable(context: Context): Boolean {

    var result = false
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            @Suppress("DEPRECATION")
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }
    return result
}

internal infix fun Uri?.extractBitmap(context: Context) = this?.run {

    if (SDK_INT < 28) {
        @Suppress("DEPRECATION")
        MediaStore.Images.Media.getBitmap(context.contentResolver, this)

    } else {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, this))
    }
}

internal fun List<File>?.toMultiPartArray() = run primaryList@{

    mutableListOf<MultipartBody.Part>().apply outList@{

        this@primaryList?.forEach { singleFile ->
            this@outList.add(
                MultipartBody.Part.createFormData(
                    name = "image",
                    filename = singleFile.name,
                    body = singleFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                )
            )
        }
    }.toTypedArray()
}

object PhotoHelper {

    @SuppressLint("Recycle", "InlinedApi")
    fun compressGetImageFilePath(
        mContext: Context,
        imageSelectedUri: Uri,
        appFolderName: String,
        createdImageCompressedName: String = System.currentTimeMillis().toString()
    ): String {
        val bitmap = if (SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(mContext.contentResolver, imageSelectedUri))
        } else {
            mContext.contentResolver.openInputStream(imageSelectedUri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        }

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, createdImageCompressedName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$appFolderName/")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val imageUri = mContext.contentResolver.insert(collection, values)

        mContext.contentResolver.openOutputStream(imageUri!!).use { out ->
            bitmap!!.compress(Bitmap.CompressFormat.JPEG, 70, out)
        }

        values.clear()
        values.put(MediaStore.Images.Media.IS_PENDING, 0)
        mContext.contentResolver.update(imageUri, values, null, null)

        val file = File(mContext.cacheDir, "$createdImageCompressedName.png")
        try {
            val assetFileDescriptor: AssetFileDescriptor =
                mContext.contentResolver.openAssetFileDescriptor(imageUri, "r")!!

            val inputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)

            inputStream.close()
            outputStream.close()


        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return file.path
    }
}
