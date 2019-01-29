package com.checki.home.ui

import android.graphics.Color
import android.text.format.DateUtils
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.checki.R
import com.checki.core.data.NetService
import com.checki.core.extensions.bind
import com.checki.core.extensions.inflate
import com.checki.core.utils.StyledStringBuilder

class NetServiceAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_SECTION = 1
        const val TYPE_ITEM    = 2
    }

    private var netServices = emptyList<NetService>()

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            0 -> TYPE_SECTION
            else -> TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_SECTION -> SectionViewHolder(parent.inflate(R.layout.item_section))
            else -> ServiceViewHolder(parent.inflate(R.layout.item_service))
        }
    }

    override fun getItemCount(): Int = netServices.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position != 0) {
            val context = holder.itemView.context
            val netService = netServices[position - 1]

            // Bind data to view holder
            holder as ServiceViewHolder
            holder.name.text = netService.name
            holder.timestamp.text = DateUtils.getRelativeTimeSpanString(
                netService.lastCheckedAt, System.currentTimeMillis(), DateUtils.DAY_IN_MILLIS)
            holder.subtext.text = StyledStringBuilder().apply {
                append("Status: ")
                if (netService.status in 200..299) {
                    appendColor(context.getString(R.string.text_ok), Color.GREEN)
                } else {
                    appendColor(context.getString(R.string.text_ok), Color.RED)
                }
                append(" (${netService.status})")
            }
        }
    }

    fun setServices(services: List<NetService>) {
        netServices = services
        notifyDataSetChanged()
    }

    class ServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: AppCompatTextView by itemView.bind(R.id.service_name)
        val subtext: AppCompatTextView by itemView.bind(R.id.service_subtext)
        val timestamp: AppCompatTextView by itemView.bind(R.id.service_timestamp)
    }

    class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}