package wafltyom.apps.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import wafltyom.apps.MainActivity
import wafltyom.apps.R
import wafltyom.apps.databinding.AboutAppBinding
import java.util.concurrent.Executors

class AboutAppFragment : ViewBindingFragment<AboutAppBinding>() {
    companion object {
        private val executor = Executors.newSingleThreadExecutor()
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = AboutAppBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context is MainActivity) {
            val vm = (context as MainActivity).vm

            vm?.chosenApp?.observe(viewLifecycleOwner) {
                executor.execute { it.requestIconDrawable(requireContext()) }
                it.calculateApkChecksum()

                binding.apply {
                    aboutAppName.text = getString(R.string.about_app_name, it.name)
                    aboutAppVersion.text = getString(R.string.about_app_version, it.version)
                    aboutAppPackageName.text =
                        getString(R.string.about_app_package_name, it.packageName)
                    it.icon.observe(viewLifecycleOwner, aboutAppIcon::setImageDrawable)
                    it.checkSum.observe(viewLifecycleOwner) { checksum ->
                        aboutAppChecksum.text = getString(R.string.about_app_checksum, checksum)
                    }
                    val intent = it.launchIntent
                    aboutAppOpenButton.setOnClickListener {
                        if (intent == null) {
                            Toast.makeText(
                                requireContext(),
                                "No launcher activity",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            startActivity(intent)
                        }

                    }

                }
            }
        }
    }
}