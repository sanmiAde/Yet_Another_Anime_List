package com.sanmidev.yetanotheranimelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.sanmidev.YetAnotherAnimeListApplication
import com.sanmidev.yetanotheranimelist.databinding.ActivityMainBinding
import com.sanmidev.yetanotheranimelist.di.component.ActivityComponent

class MainActivity : AppCompatActivity() {


     lateinit var activityComponent: ActivityComponent
    private lateinit var navController: NavController

    private var _binding : ActivityMainBinding? = null

    private val binding : ActivityMainBinding
        get()  = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        initDaggerInjection()

        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController

        initBottomNavView()
    }


    private fun initBottomNavView(){
        binding.bottomNavigationView.setupWithNavController(navController)
    }


    private fun initDaggerInjection() {
        activityComponent = (application as YetAnotherAnimeListApplication).appComponent.activityComponent().create()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
