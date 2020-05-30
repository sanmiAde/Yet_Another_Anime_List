package com.sanmidev.yetanotheranimelist.ui.common.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.sanmidev.yetanotheranimelist.data.local.model.AnimeEntity


class AnimeListDiffCallback : DiffUtil.ItemCallback<AnimeEntity>() {

    override fun areItemsTheSame(oldItem: AnimeEntity, newItem: AnimeEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AnimeEntity, newItem: AnimeEntity): Boolean {
        return oldItem == newItem
    }
}
