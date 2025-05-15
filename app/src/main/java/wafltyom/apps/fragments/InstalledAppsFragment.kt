package wafltyom.apps.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import wafltyom.apps.InstalledAppsAdapter
import wafltyom.apps.MainViewModel
import wafltyom.apps.R
import wafltyom.apps.databinding.InstalledAppsBinding

class InstalledAppsFragment : ViewBindingFragment<InstalledAppsBinding>() {
    private val vm: MainViewModel by activityViewModels()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = InstalledAppsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = InstalledAppsAdapter {
            vm.chosenApp.value = it
            Navigation.findNavController(view).navigate(R.id.aboutAppFragment)
        }

        binding.apply {
            installedAppsList.adapter = adapter
            installedAppsList.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
            swipeRefresh.setOnRefreshListener { vm.updateInstalledApps(requireContext()) }
        }

        vm.apply {
            installedApps.observe(viewLifecycleOwner) {
                adapter.submitList(it)
                binding.swipeRefresh.isRefreshing = false
            }
        }

    }
}