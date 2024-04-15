package com.blaze.shoplink.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.blaze.shoplink.Model.UserModel
import com.blaze.shoplink.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val auth=FirebaseAuth.getInstance()
    private val database=FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentProfileBinding.inflate(inflater,container,false)
        setUserData()

        binding.saveBTN.setOnClickListener {
            val name=binding.proName.text.toString()
            val email=binding.proEmail.text.toString()
            val address=binding.proAddress.text.toString()
            val phone=binding.proPhone.text.toString()

            updateUserData(name,email,address,phone)
        }

        return(binding.root)
    }

    private fun updateUserData(name: String, email: String, address: String, phone: String) {
        val userId=auth.currentUser?.uid
        if (userId !=null){
            val userReference=database.getReference("user").child(userId)

            val userData= hashMapOf(
                "name" to name,
                "address" to address,
                "email" to email,
                "phone" to phone
            )
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Profile updation Failed", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setUserData() {
        val userId=auth.currentUser?.uid
        if (userId!=null){
            val userReference=database.getReference("user").child(userId)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val userProfile=snapshot.getValue(UserModel::class.java)
                        if (userProfile !=null){
                            binding.proName.setText(userProfile.name)
                            binding.proAddress.setText(userProfile.address)
                            binding.proEmail.setText(userProfile.email)
                            binding.proPhone.setText(userProfile.phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }
}