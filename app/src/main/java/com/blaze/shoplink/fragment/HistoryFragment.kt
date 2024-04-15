package com.blaze.shoplink.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blaze.shoplink.R
import com.blaze.shoplink.adaptar.BuyAgainAdaptar
import com.blaze.shoplink.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var buyAgainAdaptar: BuyAgainAdaptar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        setupRecyclerView()

        return binding.root
    }

    private fun setupRecyclerView() {
        val buyAgainFoodName = arrayListOf("food 1", "food 2", "food 3", "food 4")
        val buyAgainFoodPrice = arrayListOf("₹10", "₹20", "₹30", "₹40")
        val buyAgainFoodImage = arrayListOf(
            R.drawable.pic1,
            R.drawable.pic2,
            R.drawable.pic3,
            R.drawable.pic4
        )
        buyAgainAdaptar=BuyAgainAdaptar(buyAgainFoodName,buyAgainFoodPrice,buyAgainFoodImage)
        binding.buyAgainRecyclerView.adapter=buyAgainAdaptar
        binding.buyAgainRecyclerView.layoutManager=LinearLayoutManager(requireContext())

    }

    companion object {

    }
}