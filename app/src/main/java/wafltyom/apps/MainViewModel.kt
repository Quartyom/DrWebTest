package wafltyom.apps

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File
import java.security.MessageDigest

class MainViewModel : ViewModel() {
    val installedApps = MutableLiveData<List<AppItem>>()
    val chosenApp = MutableLiveData<AppItem>()

    fun updateInstalledApps(context: Context) {
        Toast.makeText(context, "Список загружается...", Toast.LENGTH_SHORT).show()
        Thread {
            val packageManager = context.packageManager
            val packages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)

            val apps = ArrayList<AppItem>()

            for (info in packages) {
                val packageName = info.packageName
                val packageInfo =
                    packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
                val name =
                    packageManager.getApplicationLabel(packageInfo.applicationInfo).toString()
                val version = packageInfo.versionName
                val sourceDir = packageInfo.applicationInfo.sourceDir
                val launchIntent = packageManager.getLaunchIntentForPackage(info.packageName)

                apps.add(AppItem(name, version, packageName, sourceDir, launchIntent))

                if (apps.size % 20 == 0) {
                    installedApps.postValue(apps)
                }
            }
            apps.sortWith { a, b -> a.name.compareTo(b.name) }
            installedApps.postValue(apps)
        }.start()
    }

}