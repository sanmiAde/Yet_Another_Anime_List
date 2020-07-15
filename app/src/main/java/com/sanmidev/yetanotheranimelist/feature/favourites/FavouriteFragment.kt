package com.sanmidev.yetanotheranimelist.feature.favourites

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sanmidev.yetanotheranimelist.MainActivity
import com.sanmidev.yetanotheranimelist.R
import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.local.model.favourite.FavouriteAnimeListResult
import com.sanmidev.yetanotheranimelist.databinding.FavouriteFragmentBinding
import com.sanmidev.yetanotheranimelist.feature.airingAnimes.AnimeDetailOnClick
import com.sanmidev.yetanotheranimelist.feature.common.recyclerview.AnimeListAdapter
import com.sanmidev.yetanotheranimelist.utils.ui.navigateSafely
import com.sanmidev.yetanotheranimelist.utils.ui.showIf
import io.cabriole.decorator.ColumnProvider
import io.cabriole.decorator.GridMarginDecoration
import timber.log.Timber
import javax.inject.Inject


class FavouriteFragment : Fragment(R.layout.favourite_fragment) {

    private var favouriteFragmentBinding: FavouriteFragmentBinding? = null

    private var favouriteAnimeListAdaper: AnimeListAdapter? = null

    private val binding: FavouriteFragmentBinding
        get() = favouriteFragmentBinding!!

    companion object {
        fun newInstance() = FavouriteFragment()
    }

    @Inject
    lateinit var vmFactory: FavouriteViewModel.VMFactory

    private val viewModel by viewModels<FavouriteViewModel> { vmFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).activityComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favouriteFragmentBinding = FavouriteFragmentBinding.bind(view)
        initToolBar()
        initRecyclerView()
        observeFavouriteAnimeList()
    }

    private fun initRecyclerView() {


        val gridLayoutManager = GridLayoutManager(requireContext(), 2)


        //init adapter
        favouriteAnimeListAdaper = AnimeListAdapter(this.requireContext())
        val animePictureClickListener: AnimeDetailOnClick = { animeEntity: AnimeEntity ->
            val directions =
                FavouriteFragmentDirections.actionFavouriteFragmentToAnimeDetailFragment(
                    animeEntity.id,
                    animeEntity.imageUrl,
                    animeEntity.title
                )
//
            findNavController().navigateSafely(directions, R.id.favouriteFragment)
        }
        favouriteAnimeListAdaper?.setAnimeImageClickListener(animePictureClickListener)

        //init recyclerview
        binding.rvFavouriteAnim.apply {
            this.setHasFixedSize(true)
            this.adapter = favouriteAnimeListAdaper
            this.layoutManager = gridLayoutManager



            this.addItemDecoration(
                GridMarginDecoration(
                    margin = context.resources.getDimensionPixelSize(R.dimen.offset_size),
                    columnProvider = object : ColumnProvider {
                        override fun getNumberOfColumns(): Int = 2
                    },
                    orientation = RecyclerView.VERTICAL
                )
            )


        }
    }


    private fun observeFavouriteAnimeList() {
        viewModel.animeLiveData.observe(viewLifecycleOwner) { favouriteAnimeListResult: FavouriteAnimeListResult ->

            binding.pbFavList.showIf { favouriteAnimeListResult is FavouriteAnimeListResult.Loading }

            when (favouriteAnimeListResult) {

                is FavouriteAnimeListResult.Loaded -> {
                    favouriteAnimeListAdaper?.submitList(favouriteAnimeListResult.favAnimeList.toMutableList())
                }

                is FavouriteAnimeListResult.Error -> {
                    Timber.d(favouriteAnimeListResult.message)
                }
            }

        }
    }

    private fun initToolBar() {
        binding.include4.toolbar.title = getString(R.string.favourite_anime_txt)
    }

}
