package com.sanmidev.yetanotheranimelist.feature.animeDetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.sanmidev.yetanotheranimelist.MainActivity
import com.sanmidev.yetanotheranimelist.R
import com.sanmidev.yetanotheranimelist.data.local.model.FavouriteAnimeResult
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResult
import com.sanmidev.yetanotheranimelist.databinding.AnimeDetailFragmentBinding
import com.sanmidev.yetanotheranimelist.di.module.GlideApp
import com.sanmidev.yetanotheranimelist.feature.utils.fireToast
import com.sanmidev.yetanotheranimelist.feature.utils.gone
import com.sanmidev.yetanotheranimelist.feature.utils.visible
import javax.inject.Inject


class AnimeDetailFragment : Fragment() {

    private var detailFragmentBinding: AnimeDetailFragmentBinding? = null


    val binding: AnimeDetailFragmentBinding
        get() = detailFragmentBinding!!

    @Inject
    lateinit var savedStateViewModelFactory: AnimeDetailViewModel.VmFactory.Factory

    private val args by navArgs<AnimeDetailFragmentArgs>()

    private val viewModel by lazy {
        //GET ARGUMENT FROM PREVIOUS FRAGMENT
        val bundle = Bundle()
        val malId = args.malId
        bundle.putInt(DETAIL_ANIME_ID_KEY, malId)

        //GET VIEWMODEL
        ViewModelProvider(this, savedStateViewModelFactory.createFactory(this, bundle)).get(
            AnimeDetailViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailFragmentBinding = AnimeDetailFragmentBinding.inflate(inflater)
        return binding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).activityComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserveGetDetailState()
        initObserveIsFavouriteState()
        initOnclickListeners()

    }

    /***
     *  OBserves the network state of get anime detail from the api.
     */
    private fun initObserveGetDetailState() {
        viewModel.animeDetailResultState.observe(viewLifecycleOwner, Observer { animeDetailResult ->
            when (animeDetailResult) {
                AnimeDetailResult.Loading -> {
                    binding.pbAnimeDetail.visible()
                    binding.floatingActionButton.hide()
                }
                is AnimeDetailResult.Success -> {
                    binding.pbAnimeDetail.gone()
                    binding.floatingActionButton.show()

                    bindSuccessData(animeDetailResult)
                    viewModel.hasBeenFavourited()
                }
                is AnimeDetailResult.APIerror -> {
                    binding.pbAnimeDetail.gone()

                    fireToast(requireContext(), animeDetailResult.jikanErrorRespone.message)
                }
                is AnimeDetailResult.Exception -> {
                    binding.pbAnimeDetail.gone()

                    fireToast(requireContext(), animeDetailResult.message)
                }
            }
        })
    }

    private fun initOnclickListeners() {
        binding.floatingActionButton.setOnClickListener {
            viewModel.favouriteAnime()
        }
    }


    private fun initObserveIsFavouriteState() {
        viewModel.isFavourited.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                FavouriteAnimeResult.favourited -> {
                    binding.floatingActionButton.setImageResource(R.drawable.ic_favorite_black_24dp)
                }
                FavouriteAnimeResult.unFavourited -> {
                    binding.floatingActionButton.setImageResource(R.drawable.ic_favorite_white_24dp)
                }
                is FavouriteAnimeResult.error -> {
                    fireToast(requireContext(), getString(R.string.fav_anime_error_txt))
                }

            }
        })
    }

    /***
     * Binds the success data recieved from the Jikan API
     * [animeDetailResult] is the success result from the API.
     */
    private fun bindSuccessData(animeDetailResult: AnimeDetailResult.Success) {
        GlideApp.with(this).load(animeDetailResult.data.imageUrl)
            .into(binding.imgAnimePicture)

        binding.txtSynopsis.text = animeDetailResult.data.synopsis

        binding.collapsingToolBar.title = animeDetailResult.data.title
    }

    override fun onDetach() {
        viewModel.disposeCompositeDisposable()
        super.onDetach()
    }

    override fun onDestroy() {
        detailFragmentBinding = null
        super.onDestroy()
    }

    companion object {
        const val DETAIL_ANIME_ID_KEY =
            "com.sanmidev.yetanotheranimelist.animeDetailFragment.anime_detail_key"
    }
}
