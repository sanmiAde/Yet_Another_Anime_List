package com.sanmidev.yetanotheranimelist.feature.upComingAnimes

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sanmidev.yetanotheranimelist.MainActivity
import com.sanmidev.yetanotheranimelist.R
import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeListResult
import com.sanmidev.yetanotheranimelist.databinding.FragmentUpComingAnimesBinding
import com.sanmidev.yetanotheranimelist.feature.common.recyclerview.AnimeListAdapter
import com.sanmidev.yetanotheranimelist.utils.ui.initEndlessScrollListener
import com.sanmidev.yetanotheranimelist.utils.ui.navigateSafely
import com.sanmidev.yetanotheranimelist.utils.ui.showIf
import io.cabriole.decorator.ColumnProvider
import io.cabriole.decorator.GridMarginDecoration
import timber.log.Timber
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
//TODO create a base fragment and viewmodel for both upcoming and airing fragment
class UpComingAnimesFragment : Fragment(R.layout.fragment_up_coming_animes) {



    private var animeUpComingAnimesBinding: FragmentUpComingAnimesBinding? = null

    private var animeListAdaper: AnimeListAdapter? = null

    private val binding: FragmentUpComingAnimesBinding
        get() = animeUpComingAnimesBinding!!

    @Inject
    lateinit var vmFactory: UpComingAnimesViewModel.VMFactory

    private val viewModel by viewModels<UpComingAnimesViewModel> { vmFactory }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animeUpComingAnimesBinding = FragmentUpComingAnimesBinding.bind(view)

        initRecyclerView()
        initToolBar()
        observeNextAnimeList()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).activityComponent.inject(this)
    }


    private fun initToolBar() {
        binding.include2.toolbar.title = getString(R.string.upcoming_anime_txt)
    }

    private fun initRecyclerView() {
        //TODO add auto grid

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)



        //init adapter
        animeListAdaper = AnimeListAdapter(this.requireContext())
        val animePictureClickListener: (AnimeEntity) -> Unit = { animeEntity: AnimeEntity ->

            val directions =
                UpComingAnimesFragmentDirections.actionUpComingAnimesFragmentToAnimeDetailFragment(
                    animeEntity.id,
                    animeEntity.imageUrl,
                    animeEntity.title
                )

            findNavController().navigateSafely(directions, R.id.upComingAnimesFragment)
        }
        animeListAdaper?.setAnimeImageClickListener(animePictureClickListener)

        //init recyclerview
        binding.upComingAnimeList.apply {
            this.setHasFixedSize(true)
            this.adapter = animeListAdaper
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

            initEndlessScrollListener { viewModel.getNextUpComingAnimes() }
            // initEndlessScrollListener(gridLayoutManager)

        }

        observerGetUpComingAnimeNetworkState()
    }

    private fun observerGetUpComingAnimeNetworkState() {

        viewModel.upComingLiveData.observe(viewLifecycleOwner, Observer { animeListResult ->

            binding.progressBar.showIf { animeListResult is AnimeListResult.Loading }


            when (animeListResult) {

                is AnimeListResult.Success -> {


                    animeListAdaper?.submitList(viewModel.animeListData.toMutableList())


                }

                is AnimeListResult.APIerror -> {

                    val apiError = animeListResult.jikanErrorRespone
                    Timber.d(apiError.toString())
                }
                is AnimeListResult.Exception -> {

                    Toast.makeText(requireContext(), animeListResult.message, Toast.LENGTH_SHORT)
                        .show()

                }
            }
        })
    }

    private fun observeNextAnimeList() {
        viewModel.nextUpComingLiveData.observe(viewLifecycleOwner, Observer { animeListResult ->

            binding.progressBar.showIf { animeListResult is AnimeListResult.Loading }

            when (animeListResult) {
                is AnimeListResult.Success -> {


                    animeListAdaper?.submitList(viewModel.animeListData.toMutableList())
                }

                is AnimeListResult.APIerror -> {


                    val apiError = animeListResult.jikanErrorRespone
                    if (apiError.message != getString(R.string.res_does_not_exist)) {
                        Timber.d(apiError.message)
                    }
                }
                is AnimeListResult.Exception -> {

                    Toast.makeText(requireContext(), animeListResult.message, Toast.LENGTH_SHORT)
                        .show()

                }
            }
        })
    }




    override fun onDetach() {
        super.onDetach()
    }

    override fun onDestroyView() {
        animeListAdaper = null
        super.onDestroyView()
    }


}
