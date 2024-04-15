package com.blaze.shoplink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.blaze.shoplink.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    val navController=findNavController(R.id.fragmentContainerView)
    val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottom_bar)
    bottomNavigationView.setupWithNavController(navController)


        binding.notificationButton.setOnClickListener{
            val bottomSheetDialog=Notification_Bottom_Fragment()
            bottomSheetDialog.show(supportFragmentManager,"test")
        }
    }

}
