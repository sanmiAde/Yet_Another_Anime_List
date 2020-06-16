package com.sanmidev.yetanotheranimelist.feature.upComingAnimes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sanmidev.yetanotheranimelist.MainActivity
import com.sanmidev.yetanotheranimelist.R
import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.network.model.animelist.AnimeListResult
import com.sanmidev.yetanotheranimelist.databinding.FragmentUpComingAnimesBinding
import com.sanmidev.yetanotheranimelist.feature.airingAnimes.AiringFragmentDirections
import com.sanmidev.yetanotheranimelist.feature.common.recyclerview.AnimeListAdapter
import com.sanmidev.yetanotheranimelist.feature.utils.gone
import com.sanmidev.yetanotheranimelist.feature.utils.navigateSafely
import com.sanmidev.yetanotheranimelist.feature.utils.visible
import com.sanmidev.yetanotheranimelist.utils.EndlessRecyclerViewScrollListener
import io.cabriole.decorator.ColumnProvider
import io.cabriole.decorator.GridMarginDecoration
import timber.log.Timber
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
//TODO create a base fragment and viewmodel for both upcoming and airing fragment
class UpComingAnimesFragment : Fragment() {

    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    private var _animeUpComingAnimesBinding: FragmentUpComingAnimesBinding? = null

    private var animeListAdaper: AnimeListAdapter? = null

    private val animeUpComingAnimesBinding: FragmentUpComingAnimesBinding
        get() = _animeUpComingAnimesBinding!!

    @Inject
    lateinit var vmFactory: UpComingAnimesViewModel.VMFactory

    private val viewModel by lazy {
        ViewModelProvider(this, vmFactory)[UpComingAnimesViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _animeUpComingAnimesBinding = FragmentUpComingAnimesBinding.inflate(layoutInflater)
        return animeUpComingAnimesBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initToolBar()
        observeNextAnimeList()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).activityComponent.inject(this)
    }


    private fun initToolBar() {
        animeUpComingAnimesBinding.include2.toolbar.title = getString(R.string.upcoming_anime_txt)
    }

    private fun initRecyclerView() {
        //TODO add auto grid

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)

        initEndlessScrollListener(gridLayoutManager)

        //init adapter
        animeListAdaper = AnimeListAdapter(this.requireContext())
        val animePictureClickListener: (AnimeEntity) -> Unit = { animeEntity: AnimeEntity ->

            val directions =UpComingAnimesFragmentDirections.actionUpComingAnimesFragmentToAnimeDetailFragment(animeEntity.id, animeEntity.imageUrl, animeEntity.title)

            findNavController().navigateSafely(directions, R.id.upComingAnimesFragment)
        }
        animeListAdaper?.setAnimeImageClickListener(animePictureClickListener)

        //init recyclerview
        animeUpComingAnimesBinding.upComingAnimeList.apply {
            this.setHasFixedSize(true)
            this.adapter = animeListAdaper
            this.layoutManager = gridLayoutManager

            val spacing = context.resources.getDimensionPixelSize(R.dimen.offset_size)

            this.addItemDecoration(
                GridMarginDecoration(
                    margin = spacing,
                    columnProvider = object : ColumnProvider {
                        override fun getNumberOfColumns(): Int = 2
                    },
                    orientation = RecyclerView.VERTICAL
                )
            )

            //init grid auto layout (it's hacker though)
            this.viewTreeObserver.addOnGlobalLayoutListener(
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        animeUpComingAnimesBinding.upComingAnimeList.viewTreeObserver.removeOnGlobalLayoutListener(
                            this
                        )

                        val viewWidth: Int =
                            animeUpComingAnimesBinding.upComingAnimeList.measuredWidth
                        val cardViewWidth =
                            requireContext().resources.getDimensionPixelSize(R.dimen.anime_image_width)
                                .toInt()

                        val newSpanCount =
                            Math.floor(viewWidth / cardViewWidth.toDouble()).toInt()
                        gridLayoutManager.spanCount = newSpanCount
                        gridLayoutManager.requestLayout()
                    }
                })

            endlessRecyclerViewScrollListener?.let {
                this.addOnScrollListener(it)
            }

        }

        observerGetUpComingAnimeNetworkState()
    }

    private fun observerGetUpComingAnimeNetworkState() {
        viewModel.upComingLiveData.observe(viewLifecycleOwner, Observer { animeListResult ->
            when (animeListResult) {
                is AnimeListResult.Loading -> {

                    animeUpComingAnimesBinding.progressBar.visible()

                }
                is AnimeListResult.Success -> {

                    val animeList = animeListResult.data.animeEnities.toMutableList()
                    animeListAdaper?.submitList(animeList)
                    animeUpComingAnimesBinding.upComingAnimeList.visible()
                    animeUpComingAnimesBinding.progressBar.gone()

                }

                is AnimeListResult.APIerror -> {
                    animeUpComingAnimesBinding.progressBar.gone()

                    val apiError = animeListResult.jikanErrorRespone
                    Timber.d(apiError.toString())
                }
                is AnimeListResult.Exception -> {

                    Toast.makeText(requireContext(), animeListResult.message, Toast.LENGTH_SHORT)
                        .show()
                    animeUpComingAnimesBinding.progressBar.gone()
                }
            }
        })
    }

    private fun observeNextAnimeList() {
        viewModel.nextUpComingLiveData.observe(viewLifecycleOwner, Observer { animeListResult ->
            when (animeListResult) {
                is AnimeListResult.Loading -> {

                    animeUpComingAnimesBinding.progressBar.visible()

                }
                is AnimeListResult.Success -> {

                    animeUpComingAnimesBinding.progressBar.gone()

                    val animeList = animeListResult.data.animeEnities.toMutableList()
                    val existingAnimeList = animeListAdaper?.currentList?.toMutableList()
                    existingAnimeList?.addAll(animeList)

                    animeListAdaper?.submitList(existingAnimeList)
                }

                is AnimeListResult.APIerror -> {
                    animeUpComingAnimesBinding.progressBar.gone()

                    val apiError = animeListResult.jikanErrorRespone
                    if (apiError.message != getString(R.string.res_does_not_exist)) {
                        Timber.d(apiError.message)
                    }
                }
                is AnimeListResult.Exception -> {

                    Toast.makeText(requireContext(), animeListResult.message, Toast.LENGTH_SHORT)
                        .show()
                    animeUpComingAnimesBinding.progressBar.gone()
                }
            }
        })
    }


    private fun initEndlessScrollListener(gridLayoutManager: GridLayoutManager) {
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    viewModel.getNextUpComingAnimes()
                }

            }
    }

    override fun onDetach() {
        viewModel.cancelSubscriptions()
        super.onDetach()
    }

    override fun onDestroyView() {
        animeListAdaper = null
        super.onDestroyView()
    }


}
