package wafltyom.apps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import wafltyom.apps.databinding.AppListItemBinding
import java.util.concurrent.Executors

class InstalledAppsAdapter(
    private val onItemClick: (AppItem) -> Unit
) : ListAdapter<AppItem, InstalledAppsAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(AppListItemBinding.inflate(inflater, parent, false), onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: AppListItemBinding,
        private val onItemClick: (AppItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.getRoot()) {

        companion object {
            private val executor = Executors.newSingleThreadExecutor()
        }

        fun bind(appItem: AppItem) {
            binding.apply {
                itemAppName.text = appItem.name
                executor.execute {
                    appItem.requestIconDrawable(root.context)
                }
                if (root.context is LifecycleOwner) {
                    appItem.icon.observe(root.context as LifecycleOwner) {
                        itemIcon.setImageDrawable(it)
                    }
                }
                getRoot().setOnClickListener { onItemClick(appItem) }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<AppItem>() {
                override fun areItemsTheSame(oldItem: AppItem, newItem: AppItem): Boolean {
                    return oldItem.packageName == newItem.packageName
                }

                override fun areContentsTheSame(oldItem: AppItem, newItem: AppItem): Boolean {
                    return oldItem == newItem
                }
            }
    }
}