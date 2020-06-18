package com.sanmidev.yetanotheranimelist.feature.favourites

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sanmidev.yetanotheranimelist.R
import com.sanmidev.yetanotheranimelist.databinding.FavouriteFragmentBinding


class FavouriteFragment : Fragment() {

    private var  favouriteFragmentBinding : FavouriteFragmentBinding? = null

    private val binding : FavouriteFragmentBinding
    get() = favouriteFragmentBinding!!

    companion object {
        fun newInstance() = FavouriteFragment()
    }

    private lateinit var viewModel: FavouriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favouriteFragmentBinding = FavouriteFragmentBinding.inflate(inflater)
        return  binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavouriteViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolBar()
    }

    private fun initToolBar() {
        binding.include4.toolbar.title = getString(R.string.favourite_anime_txt)
    }

}
