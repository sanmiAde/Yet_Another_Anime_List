package com.sanmidev.yetanotheranimelist.feature.animeDetail

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
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

        GlideApp.with(this).load(args.imageUrl)
            .into(binding.imgAnimePicture)
        tweakCollaspingToolbar()

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

                    binding.floatingActionButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),  R.drawable.ic_favorite_24dp));
                }
                FavouriteAnimeResult.unFavourited -> {

                    binding.floatingActionButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),  R.drawable.ic_favorite_unfav_24dp));
                }
                is FavouriteAnimeResult.error -> {
                    fireToast(requireContext(), getString(R.string.fav_anime_error_txt))
                }

            }
        })
    }

    /***
     * Using to tweak collasping toolbar so the title of the anime only shows when the toolbar is collasped.
     */
    private fun tweakCollaspingToolbar(){
        var isShow = true
        var scrollRange = -1
        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1){
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0){
                binding.collapsingToolBar.title = args.title
                isShow = true
            } else if (isShow){
                binding.collapsingToolBar.title = " " //careful there should a space between double quote otherwise it wont work
                isShow = false
            }
        })
    }

    /***
     * Binds the success data recieved from the Jikan API
     * [animeDetailResult] is the success result from the API.
     */
    private fun bindSuccessData(animeDetailResult: AnimeDetailResult.Success) {
        binding.txtSynopsis.text = animeDetailResult.data.synopsis

       val genres =  viewModel.processGenre(animeDetailResult.data.genreEntity)

        genres.forEach { title ->
            createChip(title)
        }
    }

    private fun createChip(tag: String) {
        val chip =
            this.layoutInflater.inflate(R.layout.genre_chip, null, false) as Chip
        chip.text = tag
        val paddingDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 10f,
            resources.displayMetrics
        ).toInt()

        val generatedId = View.generateViewId()
        // All tag should be pre selected.
        chip.id = generatedId

        chip.setPadding(paddingDp, 0, paddingDp, 0)
        binding.genreChipGroup.addView(chip)


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
