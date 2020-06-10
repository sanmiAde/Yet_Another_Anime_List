package com.sanmidev.yetanotheranimelist.data.local.model.animedetail

data class RelatedEntity(

    val adaptation: List<AdaptationEntity>?,


    val prequelResponse: List<PrequelEntity>?,


    val sequelResponse: List<SequelEnitity>?
)