package com.sanmidev.yetanotheranimelist

import android.content.res.Resources
import android.os.Bundle
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
import com.sanmidev.yetanotheranimelist.utils.ui.gone
import com.sanmidev.yetanotheranimelist.utils.ui.visible
import io.reactivex.CompletableSource


class MainActivity : AppCompatActivity() {


    lateinit var activityComponent: ActivityComponent
    private lateinit var navController: NavController

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent =
            (application as YetAnotherAnimeListApplication).appComponent.activityComponent()
                .create()

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding?.bottomNavigationView.setupWithNavController(navController)
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



    override fun onDestroy() {
        super.onDestroy()
    }
}
