package com.blaze.shoplink.adaptar

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blaze.shoplink.databinding.PopularitemBinding

class PopularAdaptar(
    private val items: List<String>,
    private val price: List<String>,
    private val image: List<Int>,
    private val requireContext: Context
) : RecyclerView.Adapter<PopularAdaptar.Popularviewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Popularviewholder {
        return Popularviewholder(
            PopularitemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: Popularviewholder, position: Int) {
        val item = items[position]
        val images = image[position]
        val price = price[position]
        holder.bind(item, price, images)

//        holder.itemView.setOnClickListener{
//            val intent = Intent(requireContext, shopdetailsActivity::class.java)
//            intent.putExtra("menuItemsName", items.get(position))
//            intent.putExtra("DetailShopImage", image.get(position))
//            requireContext.startActivity(intent)      }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class Popularviewholder(private val binding: PopularitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageView = binding.imageView7
        fun bind(item: String, price: String, images: Int) {
            binding.foodnamepopular.text = item
            binding.pricepopular.text = price
            imageView.setImageResource(images)
        }

    }
}