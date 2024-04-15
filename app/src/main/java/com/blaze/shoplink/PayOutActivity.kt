package com.blaze.shoplink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blaze.shoplink.databinding.ActivityPayOutBinding
import com.blaze.shoplink.fragment.CongratsBottomSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PayOutActivity : AppCompatActivity() {
    lateinit var binding:ActivityPayOutBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var name:String
    private lateinit var address:String
    private lateinit var phone:String
    private lateinit var totalAmount:String
    private lateinit var itemName : ArrayList<String>
    private lateinit var itemPrice: ArrayList<String>
    private lateinit var itemDescription: ArrayList<String>
    private lateinit var itemImage: ArrayList<String>
    private lateinit var itemQuantities: ArrayList<Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()
        databaseReference=FirebaseDatabase.getInstance().reference

        setUserData()

        val intent=intent
        itemName=intent.getStringArrayListExtra("itemName") as ArrayList<String>
        itemPrice=intent.getStringArrayListExtra("itemPrice") as ArrayList<String>
        itemDescription=intent.getStringArrayListExtra("itemDescription") as ArrayList<String>
        itemImage=intent.getStringArrayListExtra("itemImage") as ArrayList<String>
        itemQuantities=intent.getIntegerArrayListExtra("itemQuantities") as ArrayList<Int>

        totalAmount=calculateTotalAmount().toString()+"₹"
        binding.totalAmount.isEnabled=false
        binding.totalAmount.setText(totalAmount)

        binding.placemyorder.setOnClickListener{
            val bottomSheetDialog=CongratsBottomSheet()
            bottomSheetDialog.show(supportFragmentManager,"Test")
        }
        binding.backbtn.setOnClickListener {
            finish()
        }
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount=0
        for (i in 0 until itemPrice.size){
            var price= itemPrice[i]
            val lastChar=price.last()
            val priceIntValue= if (lastChar=='₹'){
                price.dropLast(1).toInt()
            }else{
                price.toInt()
            }
            var quantity=itemQuantities[i]
            totalAmount+=priceIntValue*quantity
        }
        return totalAmount
    }

    private fun setUserData() {
        val user=auth.currentUser
        if (user != null){
            val userId=user.uid
            val userReference=databaseReference.child("user").child(userId)
            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        val name = snapshot.child("name").getValue(String::class.java) ?: ""
                        val address = snapshot.child("address").getValue(String::class.java) ?: ""
                        val phone = snapshot.child("phone").getValue(String::class.java) ?: ""

                        binding.apply {
                            Name.setText(name)
                            Address.setText(address)
                            Phone.setText(phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

    }
}