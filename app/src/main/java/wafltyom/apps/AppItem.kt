package wafltyom.apps

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import java.io.File
import java.security.MessageDigest

data class AppItem(
    val name: String,
    val version: String?,
    val packageName: String,
    val sourceDir: String?,
    val launchIntent: Intent?,
    val checkSum: MutableLiveData<String> = MutableLiveData("..."),
    val icon: MutableLiveData<Drawable> = MutableLiveData()
) {
    fun calculateApkChecksum(algorithm: String = "SHA-256") {
        if (checkSum.value != "...") {
            return
        }
        Thread {
            if (sourceDir == null) {
                checkSum.postValue("-")
            } else {
                val digest = MessageDigest.getInstance(algorithm)
                val file = File(sourceDir)
                file.inputStream().use { fis ->
                    val buffer = ByteArray(1024)
                    var data: Int
                    while (fis.read(buffer).also { data = it } != -1) {
                        digest.update(buffer, 0, data)
                    }
                }
                checkSum.postValue(digest.digest().joinToString("") { "%02x".format(it) })
            }
        }.start()
    }

    fun requestIconDrawable(context: Context) {
        if (icon.value == null) {
            icon.postValue(context.packageManager.getApplicationIcon(packageName))
        }
    }

}
