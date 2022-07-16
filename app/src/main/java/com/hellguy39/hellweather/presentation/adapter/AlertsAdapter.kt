package com.hellguy39.hellweather.presentation.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.AlertItemBinding
import com.hellguy39.hellweather.domain.model.Alert
import com.hellguy39.hellweather.format.DateFormatter
import com.hellguy39.hellweather.utils.addTagChips

class AlertsAdapter(
    private val dataSet: List<Alert>,
    private val resources: Resources
) : RecyclerView.Adapter<AlertsAdapter.AlertViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        return AlertViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.alert_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int = dataSet.size

    inner class AlertViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val binding = AlertItemBinding.bind(itemView)

        fun bind(alert: Alert) {
            binding.run {
                tvEvent.text = alert.event
                tvDateEnd.text = resources.getString(
                    R.string.text_date_end,
                    DateFormatter.format(alert.end, DateFormatter.DATE_OF_THE_MOUTH_AND_HOUR)
                )
                tvDateStart.text = resources.getString(
                    R.string.text_date_start,
                    DateFormatter.format(alert.start, DateFormatter.DATE_OF_THE_MOUTH_AND_HOUR)
                )
                tvSender.text = resources.getString(R.string.text_sender, alert.senderName)
                tvDescription.text = alert.description
                tagGroup.apply {
                    removeAllViews()
                    addTagChips(alert.tags)
                }
            }
        }
    }
}