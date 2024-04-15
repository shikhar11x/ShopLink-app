package com.blaze.shoplink.adaptar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blaze.shoplink.databinding.NotificationItemBinding

class NotificationAdaptar(private var notification:ArrayList<String>,private val notificationImage: ArrayList<Int>): RecyclerView.Adapter<NotificationAdaptar.NotificationViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
    val binding=NotificationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    return NotificationViewHolder(binding)
    }



    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
    holder.bind(position)
    }
    override fun getItemCount(): Int =notification.size

    inner class NotificationViewHolder(private val binding:NotificationItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                notificationtextView.text=notification[position]
                notificationimageView.setImageResource(notificationImage[position])
            }
        }

    }
}