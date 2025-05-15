package wafltyom.apps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import wafltyom.apps.databinding.InstalledAppsBinding

class InstalledAppsFragment : Fragment() {
    private var binding: InstalledAppsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = InstalledAppsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (context is MainActivity) {
            val vm = (context as MainActivity).vm
            val adapter = InstalledAppsAdapter(requireContext()) {
                vm?.chosenApp?.value = it
                Navigation.findNavController(view).navigate(R.id.aboutAppFragment)
            }

            binding?.apply {
                installedAppsList.adapter = adapter
                installedAppsList.addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
                swipeRefresh.setOnRefreshListener { vm?.updateInstalledApps(requireContext()) }
            }

            vm?.apply {
                installedApps.observe(viewLifecycleOwner) {
                    adapter.submitList(it)
                    binding?.swipeRefresh?.isRefreshing = false
                }
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}