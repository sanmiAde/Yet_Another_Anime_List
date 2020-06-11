package com.sanmidev.yetanotheranimelist.feature.animeDetail

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.sanmidev.yetanotheranimelist.MainActivity
import com.sanmidev.yetanotheranimelist.R
import com.sanmidev.yetanotheranimelist.data.network.model.animedetail.AnimeDetailResult
import com.sanmidev.yetanotheranimelist.databinding.AnimeDetailFragmentBinding
import com.sanmidev.yetanotheranimelist.di.module.GlideApp
import timber.log.Timber
import javax.inject.Inject


class AnimeDetailFragment : Fragment() {

    private var detailFragmentBinding : AnimeDetailFragmentBinding? = null

    val binding : AnimeDetailFragmentBinding
        get() = detailFragmentBinding!!

    @Inject
    lateinit var savedStateViewModelFactory: AnimeDetailViewModel.VmFactory.Factory

    private val args by navArgs<AnimeDetailFragmentArgs>()

    private val viewModel by lazy {

        val bundle = Bundle()
        val malId = args.malId
        bundle.putInt(DETAIL_ANIME_ID_KEY, malId)


        ViewModelProvider(this, savedStateViewModelFactory.createFactory(this, bundle )).get(AnimeDetailViewModel::class.java)
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailFragmentBinding = AnimeDetailFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).activityComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.animeDetailResultState.observe(viewLifecycleOwner, Observer {animeDetailResult ->
            when(animeDetailResult){
                AnimeDetailResult.Loading -> {
                    Timber.d("Loading")
                }
                is AnimeDetailResult.Success -> {
                    Timber.d(animeDetailResult.data.imageUrl)
                    Timber.d( animeDetailResult.data.synopsis)

                    GlideApp.with(this).load(animeDetailResult.data.imageUrl).into(binding.imgAnimePicture)

                    binding.txtSynopsis.text = animeDetailResult.data.synopsis
                }
                is AnimeDetailResult.APIerror -> {
                    Timber.d(animeDetailResult.jikanErrorRespone.toString())
                }
                is AnimeDetailResult.Exception -> {
                    Timber.d(animeDetailResult.message)
                    Timber.d(animeDetailResult.throwable)
                }
            }
        })

    }


    override fun onDestroy() {
        detailFragmentBinding = null
        super.onDestroy()
    }

    companion object {
        const val DETAIL_ANIME_ID_KEY = "com.sanmidev.yetanotheranimelist.animeDetailFragment.anime_detail_key"
    }
}
