package com.sanmidev.yetanotheranimelist.feature.common.recyclerview


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.sanmidev.yetanotheranimelist.data.local.model.animelist.AnimeEntity
import com.sanmidev.yetanotheranimelist.databinding.AnimeListItemBinding
import com.sanmidev.yetanotheranimelist.di.module.GlideApp
import com.sanmidev.yetanotheranimelist.feature.airingAnimes.AnimeDetailOnClick

class AnimeListAdapter(val context : Context) : ListAdapter<AnimeEntity, AnimeListAdapter.AnimeListViewHolder>(AnimeListDiffCallback()) {

    private lateinit var onAnimeImageClickListener :  (AnimeEntity) -> Unit

    inner  class AnimeListViewHolder(val binding : AnimeListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            animeEntity: AnimeEntity
        ){

            GlideApp
                .with(context)
                .load(animeEntity.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imgAnime)

            binding.txtAnimeTitle.text = animeEntity.title



        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeListViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = AnimeListItemBinding.inflate(layoutInflater, parent, false)

        val holder = AnimeListViewHolder(binding)

        binding.root.setOnClickListener {

            onAnimeImageClickListener.invoke(getItem(holder.adapterPosition))
        }

        return holder
    }

    override fun onBindViewHolder(holder: AnimeListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    /***
     * This method is overridden because of  how  submitList is implemented.
     * List adapter does not determine if a previously submitted list contains different items
     * So we create submit our list wrapped in an arraylist.
     * @author oluwasanmi Aderibigbe
     */

    override fun submitList(list: MutableList<AnimeEntity>?) {
        super.submitList(list?.let { ArrayList(it) })
    }


    fun setAnimeImageClickListener(listener: AnimeDetailOnClick) {
        onAnimeImageClickListener = listener
    }
}