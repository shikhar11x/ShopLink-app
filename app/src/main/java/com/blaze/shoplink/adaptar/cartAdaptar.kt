package com.blaze.shoplink.adaptar

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.blaze.shoplink.databinding.CartitemBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class cartAdaptar(
    private val context: Context,
    private val cartItems: MutableList<String>,
    private val cartItemPrice: MutableList<String>,
    private var cartDescription: MutableList<String>,
    private var cartImage: MutableList<String>,
    private val cartQuantity: MutableList<Int>
) : RecyclerView.Adapter<cartAdaptar.cartviewHolder>() {

    private val auth = FirebaseAuth.getInstance()

    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        val cartItemNumber = cartItems.size

        itemQuantities = IntArray(cartItemNumber) { 1 }
        cartItemReference = database.reference.child("user").child(userId).child("CartItems")
    }

    companion object {
        private var itemQuantities: IntArray = intArrayOf()
        private lateinit var cartItemReference: DatabaseReference
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartviewHolder {
        val binding = CartitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return cartviewHolder(binding)
    }

    override fun onBindViewHolder(holder: cartviewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cartItems.size
    fun getUpdatedItemQuantities(): MutableList<Int> {
        val itemQuantity = mutableListOf<Int>()
        itemQuantity.addAll(cartQuantity)
        return itemQuantity
    }

    inner class cartviewHolder(private val binding: CartitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                cartfoodname.text = cartItems[position]
                cartitemprice.text = cartItemPrice[position]
                cartitemquantity.text = quantity.toString()

//                loading image using GLIDE
                val uriString = cartImage[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(cartimage)

                minusbutton.setOnClickListener {
                    decreaseQuantity(position)

                }
                plusbutton.setOnClickListener {
                    increaseQuantity(position)
                }
                deleteButton.setOnClickListener {
                    val itemPostion = adapterPosition
                    if (itemPostion != RecyclerView.NO_POSITION) {
                        deleteitem(position)
                    }
                }

            }

        }

        private fun decreaseQuantity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                cartQuantity[position]= itemQuantities[position]
                binding.cartitemquantity.text = itemQuantities[position].toString()
            }
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                cartQuantity[position]= itemQuantities[position]

                binding.cartitemquantity.text = itemQuantities[position].toString()
            }
        }

        private fun deleteitem(position: Int) {
            val positionRetrieve = position
            getUniqueKeyAtPosition(positionRetrieve) { uniquekey ->
                if (uniquekey != null) {
                    removeItem(position, uniquekey)
                }

            }
        }

        private fun removeItem(position: Int, uniquekey: String) {
            if (uniquekey != null) {
                cartItemReference.child(uniquekey).removeValue().addOnSuccessListener {
                    cartItems.removeAt(position)
                    cartImage.removeAt(position)
                    cartDescription.removeAt(position)
                    cartQuantity.removeAt(position)
                    cartItemPrice.removeAt(position)
                    Toast.makeText(context, "Item Removed Successfully", Toast.LENGTH_SHORT).show()
                    itemQuantities =
                        itemQuantities.filterIndexed { index, i -> index != position }.toIntArray()
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, cartItems.size)
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun getUniqueKeyAtPosition(positionRetrieve: Int, onComplete: (String?) -> Unit) {
            cartItemReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniquekey: String? = null
                    snapshot.children.forEachIndexed { index, dataSnapshot ->
                        if (index == positionRetrieve) {
                            uniquekey = dataSnapshot.key
                            return@forEachIndexed

                        }
                    }
                    onComplete(uniquekey)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }

    }
}