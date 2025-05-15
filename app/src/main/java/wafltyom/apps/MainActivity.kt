package wafltyom.apps

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import wafltyom.apps.databinding.InstalledAppsBinding

class MainActivity : AppCompatActivity() {
    var vm: MainViewModel? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vm = ViewModelProvider(this)[MainViewModel::class.java]
        vm?.apply {
            if (installedApps.value == null) {
                updateInstalledApps(this@MainActivity)
            }
        }
    }
}