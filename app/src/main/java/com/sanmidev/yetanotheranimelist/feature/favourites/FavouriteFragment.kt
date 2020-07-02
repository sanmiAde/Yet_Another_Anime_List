package com.sanmidev.yetanotheranimelist.feature.favourites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.sanmidev.yetanotheranimelist.R
import com.sanmidev.yetanotheranimelist.databinding.FavouriteFragmentBinding


class FavouriteFragment : Fragment(R.layout.favourite_fragment) {

    private var favouriteFragmentBinding: FavouriteFragmentBinding? = null

    private val binding: FavouriteFragmentBinding
        get() = favouriteFragmentBinding!!

    companion object {
        fun newInstance() = FavouriteFragment()
    }

    private lateinit var viewModel: FavouriteViewModel



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavouriteViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouriteFragmentBinding = FavouriteFragmentBinding.bind(view)
        initToolBar()
    }

    private fun initToolBar() {
        binding.include4.toolbar.title = getString(R.string.favourite_anime_txt)
    }

}
