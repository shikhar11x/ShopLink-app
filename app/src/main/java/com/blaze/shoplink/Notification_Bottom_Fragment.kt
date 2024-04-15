package com.blaze.shoplink

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blaze.shoplink.adaptar.NotificationAdaptar
import com.blaze.shoplink.databinding.FragmentNotificationBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class Notification_Bottom_Fragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentNotificationBottomBinding.inflate(layoutInflater,container,false)
        val notification= listOf("Your order has been Canceled Successfully","Order has been taken by the driver","Congrats Your Order Placed")
        val notificationImage= listOf(R.drawable.sademoji,R.drawable.icon__2_,R.drawable.illustration)
        val adt=NotificationAdaptar(ArrayList(notification), ArrayList(notificationImage))
        binding.notirv.layoutManager= LinearLayoutManager(requireContext())
        binding.notirv.adapter=adt
        return binding.root
    }
    }