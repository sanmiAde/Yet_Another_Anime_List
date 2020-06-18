package com.sanmidev.yetanotheranimelist

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.mikhaellopez.rxanimation.fadeIn
import com.mikhaellopez.rxanimation.fadeOut
import com.sanmidev.YetAnotherAnimeListApplication
import com.sanmidev.yetanotheranimelist.databinding.ActivityMainBinding
import com.sanmidev.yetanotheranimelist.di.component.ActivityComponent
import com.sanmidev.yetanotheranimelist.feature.utils.gone
import com.sanmidev.yetanotheranimelist.feature.utils.visible
import io.reactivex.CompletableSource
import timber.log.Timber


class MainActivity : AppCompatActivity() {


    lateinit var activityComponent: ActivityComponent
    private lateinit var navController: NavController

    private var _binding: ActivityMainBinding? = null

    private val binding: ActivityMainBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        initDaggerInjection()

        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController

        initBottomNavView()
        initDestinationListner()

    }

    /***
     * @author oluwasanmi Aderibigbe
     *
     */
    private fun initDestinationListner() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            val dest: String = try {
                resources.getResourceName(destination.id)
            } catch (e: Resources.NotFoundException) {
                destination.id.toString()
            }

            //Enables all the menu options in the navbar
            binding.bottomNavigationView.menu.iterator().forEach { menuItem ->
                menuItem.isEnabled = true
            }


                val menu = binding.bottomNavigationView.menu.findItem(destination.id)
                menu?.isEnabled = false


            when (destination.id) {
                R.id.animeDetailFragment -> {
                    binding.bottomNavigationView.fadeOut().andThen(CompletableSource { binding.bottomNavigationView.gone()  }).subscribe()
                }
                else -> {
                   binding.bottomNavigationView.visible()
                    binding.bottomNavigationView.fadeIn().subscribe()
                }
            }
        }
    }


    private fun initBottomNavView() {
        binding.bottomNavigationView.setupWithNavController(navController)
    }


    private fun initDaggerInjection() {
        activityComponent =
            (application as YetAnotherAnimeListApplication).appComponent.activityComponent()
                .create()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
