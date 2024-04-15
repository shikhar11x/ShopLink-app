package com.blaze.shoplink.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blaze.shoplink.MenuBottomSheetFragment
import com.blaze.shoplink.Model.MenuItem
import com.blaze.shoplink.R
import com.blaze.shoplink.adaptar.MenuAdapter
import com.blaze.shoplink.databinding.FragmentHomeBinding
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.viewAllMenu.setOnClickListener {
            val bottomsheetdialog = MenuBottomSheetFragment()
            bottomsheetdialog.show(parentFragmentManager, "Test")
        }

        retireveAndDisplayPopularItems()
        return binding.root
    }

    private fun retireveAndDisplayPopularItems() {
        database = FirebaseDatabase.getInstance()
        val itemRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()
        itemRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    val menuItem = itemSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
                randomPopularItems()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun randomPopularItems() {
        val index=menuItems.indices.toList().shuffled()
        val numItems=6
        val subsetMenuItems=index.take(numItems).map { menuItems[it] }
        setPopularAdapter(subsetMenuItems)
    }

    private fun setPopularAdapter(subsetMenuItems:List<MenuItem>) {
        val adapter = MenuAdapter(subsetMenuItems, requireContext())
        binding.HomeRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.HomeRecyclerview.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.poster1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.post1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.post, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun doubleClick(position: Int) {
            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "Selected image $position"
                Toast.makeText(requireContext(), itemMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}