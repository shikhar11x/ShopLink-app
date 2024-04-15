package com.blaze.shoplink.adaptar

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blaze.shoplink.DetailsActivity
import com.blaze.shoplink.Model.MenuItem
import com.blaze.shoplink.databinding.MenuitemBinding
import com.bumptech.glide.Glide

class MenuAdapter(
    private val menuItems: List<MenuItem>,
    private val requireContext: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = MenuitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewHolder(private val binding: MenuitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    openDetailsActivity(position)

                }
            }
        }

        private fun openDetailsActivity(position: Int) {

            val menuItem = menuItems[position]

            val intent = Intent(requireContext, DetailsActivity::class.java).apply {
                putExtra("menuItemName", menuItem.itemName)
                putExtra("menuItemImage", menuItem.itemImage)
                putExtra("menuItemDescription", menuItem.itemDescription)
                putExtra("menuItemPrice", menuItem.itemPrice)
            }
            requireContext.startActivity(intent)
        }

        fun bind(position: Int) {
            val menuItem = menuItems[position]
            binding.apply {
                menuFoodName.text = menuItem.itemName
                menuPrice.text = menuItem.itemPrice
                val uri= Uri.parse(menuItem.itemImage)
                Glide.with(requireContext).load(uri).into(menuImage)
            }
        }
    }
}





