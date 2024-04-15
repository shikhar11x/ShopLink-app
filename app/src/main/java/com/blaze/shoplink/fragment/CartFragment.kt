package com.blaze.shoplink.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blaze.shoplink.Model.CartItems
import com.blaze.shoplink.PayOutActivity
import com.blaze.shoplink.adaptar.cartAdaptar
import com.blaze.shoplink.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var itemName: MutableList<String>
    private lateinit var itemPrice: MutableList<String>
    private lateinit var itemDescription: MutableList<String>
    private lateinit var itemImageUri: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var cartAdaptar: cartAdaptar
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        auth= FirebaseAuth.getInstance()

        retrieveCartItems()
        binding.proceedButton.setOnClickListener {

            getOrderItemDetails()
           }

        return binding.root
    }

    private fun getOrderItemDetails() {

        val orderIdReference:DatabaseReference=database.reference.child("user").child(userId).child("CartItems")

        val itemName= mutableListOf<String>()
        val itemPrice= mutableListOf<String>()
        val itemImage= mutableListOf<String>()
        val itemDescription= mutableListOf<String>()
        val itemQuantities= cartAdaptar.getUpdatedItemQuantities()

        orderIdReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (itemSnapshot in snapshot.children){
                    val orderItems=itemSnapshot.getValue(CartItems::class.java)
                    orderItems?.itemName?.let { itemName.add(it) }
                    orderItems?.itemPrice?.let { itemPrice.add(it) }
                    orderItems?.itemDescription?.let { itemDescription.add(it) }
                    orderItems?.itemImage?.let { itemImage.add(it) }
                    orderItems?.itemName?.let { itemName.add(it) }
                }
                orderNow(itemName,itemPrice,itemDescription,itemImage,itemQuantities)
            }

            private fun orderNow(
                itemName: MutableList<String>,
                itemPrice: MutableList<String>,
                itemDescription: MutableList<String>,
                itemImage: MutableList<String>,
                itemQuantities: MutableList<Int>
            ) {
                if (isAdded && context !=null){
                    val intent=Intent(requireContext(),PayOutActivity::class.java)
                    intent.putExtra("itemName",itemName as ArrayList<String>)
                    intent.putExtra("itemPrice",itemPrice as ArrayList<String>)
                    intent.putExtra("itemDescription",itemDescription as ArrayList<String>)
                    intent.putExtra("itemImage",itemImage as ArrayList<String>)
                    intent.putExtra("itemQuantities",itemQuantities as ArrayList<Int>)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Order Placing Failed.. Try Again!", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun retrieveCartItems() {
        database= FirebaseDatabase.getInstance()
        userId=auth.currentUser?.uid?:""
        val foodReference:DatabaseReference=database.reference.child("user").child(userId).child("CartItems")
        itemName= mutableListOf()
        itemPrice= mutableListOf()
        itemDescription= mutableListOf()
        itemImageUri= mutableListOf()
        quantity= mutableListOf()

        foodReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children){
                    val cartItems=itemSnapshot.getValue(CartItems::class.java)
                    cartItems?.itemName?.let { itemName.add(it) }
                    cartItems?.itemPrice?.let { itemPrice.add(it) }
                    cartItems?.itemDescription?.let { itemDescription.add(it) }
                    cartItems?.itemImage?.let { itemImageUri.add(it) }
                    cartItems?.itemQuantity?.let { quantity.add(it) }
                }
                setAdapter()
            }

            private fun setAdapter() {

                cartAdaptar = cartAdaptar(requireContext(),itemName,itemPrice,itemDescription,itemImageUri,quantity)
                binding.cartrecyclerview.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                binding.cartrecyclerview.adapter = cartAdaptar


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Data not fetch", Toast.LENGTH_SHORT).show()
            }

        })


    }

    companion object {

    }
}