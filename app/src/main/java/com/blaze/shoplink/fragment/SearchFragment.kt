package com.blaze.shoplink.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blaze.shoplink.Model.MenuItem
import com.blaze.shoplink.adaptar.MenuAdapter
import com.blaze.shoplink.databinding.FragmentSearchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter
    private lateinit var database: FirebaseDatabase
    private val originalMenuItems = mutableListOf<MenuItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        retrieveMenuItem()
        setupSearchView()
        return binding.root
    }

    private fun retrieveMenuItem() {
        database = FirebaseDatabase.getInstance()
        val itemReference: DatabaseReference = database.reference.child("menu")
        itemReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    val menuItem = itemSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let {
                        originalMenuItems.add(it)
                    }
                }
                showAllMenu()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun showAllMenu() {
        val filterMenuItem = ArrayList(originalMenuItems)
        setAdapter(filterMenuItem)
    }

    private fun setAdapter(filterMenuItem: List<MenuItem>) {
        adapter = MenuAdapter(filterMenuItem, requireContext())
        binding.menuRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerview.adapter = adapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItem(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItem(newText)
                return true
            }
        })
    }

    private fun filterMenuItem(query: String) {
        val filterMenuItem=originalMenuItems.filter {
            it.itemName?.contains(query,ignoreCase = true)==true
        }
        setAdapter(filterMenuItem)
    }
}


