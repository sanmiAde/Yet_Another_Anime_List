package com.sanmidev.yetanotheranimelist.presentation.upComingAnimes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.sanmidev.yetanotheranimelist.MainActivity
import com.sanmidev.yetanotheranimelist.R
import com.sanmidev.yetanotheranimelist.data.local.model.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListResult
import com.sanmidev.yetanotheranimelist.databinding.FragmentUpComingAnimesBinding
import com.sanmidev.yetanotheranimelist.presentation.common.recyclerview.AnimeListAdapter
import com.sanmidev.yetanotheranimelist.presentation.common.recyclerview.AnimeListDecorator
import com.sanmidev.yetanotheranimelist.presentation.utils.gone
import com.sanmidev.yetanotheranimelist.presentation.utils.visible
import timber.log.Timber
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class UpComingAnimesFragment : Fragment() {

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
        val view = animeUpComingAnimesBinding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).activityComponent.inject(this)
    }

    private fun initRecyclerView() {
        val gridLayoutManager = GridLayoutManager(this.requireContext(), 2)

        animeListAdaper = AnimeListAdapter(this.requireContext())

        val animePictureClickListener : (AnimeEntity) -> Unit = {animeEntity: AnimeEntity ->
            Timber.d(animeEntity.toString())
        }
        animeListAdaper?.setAnimeImageClickListener(animePictureClickListener)

        animeUpComingAnimesBinding.upComingAnimeList.apply {
            this.layoutManager = gridLayoutManager
            this.setHasFixedSize(true)
            this.adapter = animeListAdaper

            val offSetSize = context.resources.getInteger(R.integer.offset_size)
            this.addItemDecoration(AnimeListDecorator(offSetSize))
        }

        observerGetUpComingAnimeNetworkState()
    }

    private fun observerGetUpComingAnimeNetworkState() {
        viewModel.upComingLiveData.observe(viewLifecycleOwner, Observer { animeListResult ->
            when (animeListResult) {
                is AnimeListResult.loading -> {

                    animeUpComingAnimesBinding.progressBar.visible()

                }
                is AnimeListResult.success -> {

                    animeUpComingAnimesBinding.progressBar.gone()

                    val animeList = animeListResult.data.animeEnities.toMutableList()
                    animeListAdaper?.submitList(animeList)
                }

                is AnimeListResult.APIerror -> {
                    animeUpComingAnimesBinding.progressBar.gone()

                    val apiError = animeListResult.animeListErrorRespones
                    Timber.d(apiError.toString())
                }
                is AnimeListResult.Exception -> {
                    animeUpComingAnimesBinding.progressBar.gone()
                }
            }
        })
    }


    override fun onDestroyView() {
        animeListAdaper = null
        super.onDestroyView()

    }
}
