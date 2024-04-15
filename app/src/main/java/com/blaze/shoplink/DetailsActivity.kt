package com.blaze.shoplink

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blaze.shoplink.Model.CartItems
import com.blaze.shoplink.databinding.ActivityDetailsBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private var itemName: String? = null
    private var itemImage: String? = null
    private var itemDescription: String? = null
    private var itemPrice: String? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()

        itemName = intent.getStringExtra("menuItemName")
        itemDescription = intent.getStringExtra("menuItemDescription")
        itemPrice = intent.getStringExtra("menuItemPrice")
        itemImage = intent.getStringExtra("menuItemImage")

        with(binding) {
            detailsFoodName.text = itemName
            DescriptiontextView.text = itemDescription
            IngredientTextView.text = itemPrice
            Glide.with(this@DetailsActivity).load(Uri.parse(itemImage)).into(DetailFoodImage)
        }


        binding.imageButton4.setOnClickListener {
            finish()
        }
        binding.ATC.setOnClickListener {
            addItemToCart()
        }
    }

    private fun addItemToCart() {
        val database=FirebaseDatabase.getInstance().reference
        val userId=auth.currentUser?.uid?:""
        val cartItem = CartItems(itemName.toString(),itemPrice.toString(),itemDescription.toString(),itemImage.toString(),1)


//       saving cart Data to firebase
        database.child("user").child(userId).child("CartItems").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this, "Item Added To Cart", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }


    }
}