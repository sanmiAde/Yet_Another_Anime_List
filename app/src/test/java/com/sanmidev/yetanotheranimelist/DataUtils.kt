package com.sanmidev.yetanotheranimelist

import com.github.javafaker.Faker
import com.sanmidev.yetanotheranimelist.data.local.model.AnimeEntity
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListErrorRespones
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeListResponse
import com.sanmidev.yetanotheranimelist.data.network.model.AnimeResponse

object DataUtils {

    fun generateAnimeReponse(faker: Faker): Pair<MutableList<AnimeResponse>, MutableList<AnimeEntity>> {
        val animeReponseList = mutableListOf<AnimeResponse>()
        val animeEnityList = mutableListOf<AnimeEntity>()


        for (i in 1..50) {
            val endDate = faker.date().birthday().toString()
            val episodes = faker.number().randomDigit()
            val imageUrl = faker.internet().url()
            val malId = faker.number().randomDigit()
            val members = faker.number().randomDigit()
            val rank = faker.number().randomDigit()
            val score = faker.number().randomDigit().toDouble()
            val startDate = faker.date().birthday().toString()
            val title = faker.name().title()
            val type = faker.music().genre()
            val url = faker.internet().url()

            val animeResponse =
                AnimeResponse(
                    endDate,
                    episodes,
                    imageUrl,
                    malId,
                    members,
                    rank,
                    score,
                    startDate,
                    title,
                    type,
                    url
                )


            val animeEntity = AnimeEntity(
                endDate, episodes, imageUrl, malId, score, startDate, title, type, url
            )

            animeReponseList.add(animeResponse)

            animeEnityList.add(animeEntity)
        }

        return Pair(animeReponseList, animeEnityList)
    }


    fun generateAnimeListResponse(faker: Faker): Triple<AnimeListResponse, List<AnimeResponse>, List<AnimeEntity>> {

        val animeLists = generateAnimeReponse(faker)


        return Triple(
            AnimeListResponse(
                faker.hashCode(), true, faker.hashCode().toString(),
                animeLists.first

            ), animeLists.first, animeLists.second
        )
    }

    fun getAnimeListErrorResponse() : AnimeListErrorRespones{
       return AnimeListErrorRespones("Resource does not exist", "Something Happened", 404, "BadResponseException")
    }


    val animeListtestJSonData = """
        {
          "request_hash": "request:top:3506eaba6445f7ad5cc2f78417bf6ed916b6aaad",
          "request_cached": true,
          "request_cache_expiry": 10020,
          "top": [
            {
              "mal_id": 39587,
              "rank": 1,
              "title": "Re:Zero kara Hajimeru Isekai Seikatsu 2nd Season",
              "url": "https:\/\/myanimelist.net\/anime\/39587\/Re_Zero_kara_Hajimeru_Isekai_Seikatsu_2nd_Season",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1010\/100084.jpg?s=d4e5ed624b432efaf3c328ab31a29c00",
              "type": "TV",
              "episodes": null,
              "start_date": "Jul 2020",
              "end_date": null,
              "members": 206622,
              "score": 0
            },
            {
              "mal_id": 40456,
              "rank": 2,
              "title": "Kimetsu no Yaiba Movie: Mugen Ressha-hen",
              "url": "https:\/\/myanimelist.net\/anime\/40456\/Kimetsu_no_Yaiba_Movie__Mugen_Ressha-hen",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1704\/106947.jpg?s=685b7fa652f5b3df29bd20fc2c8cb32e",
              "type": "Movie",
              "episodes": 1,
              "start_date": "Oct 2020",
              "end_date": "Oct 2020",
              "members": 153496,
              "score": 0
            },
            {
              "mal_id": 39547,
              "rank": 3,
              "title": "Yahari Ore no Seishun Love Comedy wa Machigatteiru. Kan",
              "url": "https:\/\/myanimelist.net\/anime\/39547\/Yahari_Ore_no_Seishun_Love_Comedy_wa_Machigatteiru_Kan",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1910\/106073.jpg?s=72c8c29be662919f1c78ae1d241955f7",
              "type": "TV",
              "episodes": null,
              "start_date": "Jul 2020",
              "end_date": null,
              "members": 140406,
              "score": 0
            },
            {
              "mal_id": 3786,
              "rank": 4,
              "title": "Evangelion: 3.0+1.0",
              "url": "https:\/\/myanimelist.net\/anime\/3786\/Evangelion__30_10",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1929\/107081.jpg?s=cb3a7d51589d20b83d7dbc30a61df027",
              "type": "Movie",
              "episodes": 1,
              "start_date": null,
              "end_date": null,
              "members": 132111,
              "score": 0
            },
            {
              "mal_id": 39617,
              "rank": 5,
              "title": "Yakusoku no Neverland 2nd Season",
              "url": "https:\/\/myanimelist.net\/anime\/39617\/Yakusoku_no_Neverland_2nd_Season",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1708\/104922.jpg?s=ca271334b66a079450e62ad173c75560",
              "type": "TV",
              "episodes": null,
              "start_date": "Jan 2021",
              "end_date": null,
              "members": 131796,
              "score": 0
            },
            {
              "mal_id": 39551,
              "rank": 6,
              "title": "Tensei shitara Slime Datta Ken 2nd Season",
              "url": "https:\/\/myanimelist.net\/anime\/39551\/Tensei_shitara_Slime_Datta_Ken_2nd_Season",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1530\/106442.jpg?s=273166f91e44a887b0e0e0e2257e21e5",
              "type": "TV",
              "episodes": null,
              "start_date": "Jan 2021",
              "end_date": "Mar 2021",
              "members": 125725,
              "score": 0
            },
            {
              "mal_id": 40356,
              "rank": 7,
              "title": "Tate no Yuusha no Nariagari 2nd Season",
              "url": "https:\/\/myanimelist.net\/anime\/40356\/Tate_no_Yuusha_no_Nariagari_2nd_Season",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1008\/102902.jpg?s=a28447b83a1b4ca415a703a9d89562cc",
              "type": "TV",
              "episodes": null,
              "start_date": null,
              "end_date": null,
              "members": 119098,
              "score": 0
            },
            {
              "mal_id": 40028,
              "rank": 8,
              "title": "Shingeki no Kyojin The Final Season",
              "url": "https:\/\/myanimelist.net\/anime\/40028\/Shingeki_no_Kyojin_The_Final_Season",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1873\/103180.jpg?s=9d519ed970c4e35d5ace7b6b5796462d",
              "type": "TV",
              "episodes": null,
              "start_date": "Oct 2020",
              "end_date": null,
              "members": 108402,
              "score": 0
            },
            {
              "mal_id": 40852,
              "rank": 9,
              "title": "Dr. Stone: Stone Wars",
              "url": "https:\/\/myanimelist.net\/anime\/40852\/Dr_Stone__Stone_Wars",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1379\/104911.jpg?s=4838dd907c029de310fa13eb3779880d",
              "type": "TV",
              "episodes": null,
              "start_date": "2020",
              "end_date": null,
              "members": 106349,
              "score": 0
            },
            {
              "mal_id": 40540,
              "rank": 10,
              "title": "Sword Art Online: Alicization - War of Underworld 2nd Season",
              "url": "https:\/\/myanimelist.net\/anime\/40540\/Sword_Art_Online__Alicization_-_War_of_Underworld_2nd_Season",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1438\/105106.jpg?s=fd2b796285429608f568d4a79209cece",
              "type": "TV",
              "episodes": 11,
              "start_date": "Jul 2020",
              "end_date": null,
              "members": 105312,
              "score": 0
            },
            {
              "mal_id": 37987,
              "rank": 11,
              "title": "Violet Evergarden Movie",
              "url": "https:\/\/myanimelist.net\/anime\/37987\/Violet_Evergarden_Movie",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1225\/106549.jpg?s=d7ac7fb7563cb8eeda52ff84cf0b4aa2",
              "type": "Movie",
              "episodes": 1,
              "start_date": "2020",
              "end_date": "2020",
              "members": 103638,
              "score": 0
            },
            {
              "mal_id": 40454,
              "rank": 12,
              "title": "Dungeon ni Deai wo Motomeru no wa Machigatteiru Darou ka III",
              "url": "https:\/\/myanimelist.net\/anime\/40454\/Dungeon_ni_Deai_wo_Motomeru_no_wa_Machigatteiru_Darou_ka_III",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1791\/103303.jpg?s=97419499251512e32998660aea39a9f8",
              "type": "TV",
              "episodes": null,
              "start_date": "Jul 2020",
              "end_date": null,
              "members": 81699,
              "score": 0
            },
            {
              "mal_id": 39783,
              "rank": 13,
              "title": "5-toubun no Hanayome ∬",
              "url": "https:\/\/myanimelist.net\/anime\/39783\/5-toubun_no_Hanayome_∬",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1234\/106118.jpg?s=441f854d5387c17eab919e31439cfd3e",
              "type": "TV",
              "episodes": null,
              "start_date": "Jan 2021",
              "end_date": null,
              "members": 81407,
              "score": 0
            },
            {
              "mal_id": 33050,
              "rank": 14,
              "title": "Fate\/stay night Movie: Heaven's Feel - III. Spring Song",
              "url": "https:\/\/myanimelist.net\/anime\/33050\/Fate_stay_night_Movie__Heavens_Feel_-_III_Spring_Song",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1400\/104685.jpg?s=ed1267e37c7a311d39444a0eb37f75d5",
              "type": "Movie",
              "episodes": 1,
              "start_date": null,
              "end_date": null,
              "members": 71103,
              "score": 0
            },
            {
              "mal_id": 40956,
              "rank": 15,
              "title": "Enen no Shouboutai: Ni no Shou",
              "url": "https:\/\/myanimelist.net\/anime\/40956\/Enen_no_Shouboutai__Ni_no_Shou",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1673\/107657.jpg?s=4acf01a0c3b27772cd47bc22f9ec46ef",
              "type": "TV",
              "episodes": null,
              "start_date": "Jul 2020",
              "end_date": null,
              "members": 65483,
              "score": 0
            },
            {
              "mal_id": 39247,
              "rank": 16,
              "title": "Kobayashi-san Chi no Maid Dragon 2nd Season",
              "url": "https:\/\/myanimelist.net\/anime\/39247\/Kobayashi-san_Chi_no_Maid_Dragon_2nd_Season",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1609\/99019.jpg?s=9f4be9b5190f5cbc9864fa4b9e6f1940",
              "type": "TV",
              "episodes": null,
              "start_date": null,
              "end_date": null,
              "members": 61677,
              "score": 0
            },
            {
              "mal_id": 40776,
              "rank": 17,
              "title": "Haikyuu!!: To the Top 2nd Season",
              "url": "https:\/\/myanimelist.net\/anime\/40776\/Haikyuu__To_the_Top_2nd_Season",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1453\/106768.jpg?s=08662a9ed32a8637dbdfa92f67949078",
              "type": "TV",
              "episodes": 12,
              "start_date": null,
              "end_date": null,
              "members": 60877,
              "score": 0
            },
            {
              "mal_id": 39586,
              "rank": 18,
              "title": "Hataraku Saibou!!",
              "url": "https:\/\/myanimelist.net\/anime\/39586\/Hataraku_Saibou",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1624\/106600.jpg?s=5cf208f5df2d060cfc53d3dff9aa8962",
              "type": "TV",
              "episodes": null,
              "start_date": "Jan 2021",
              "end_date": null,
              "members": 51749,
              "score": 0
            },
            {
              "mal_id": 41587,
              "rank": 19,
              "title": "Boku no Hero Academia 5th Season",
              "url": "https:\/\/myanimelist.net\/anime\/41587\/Boku_no_Hero_Academia_5th_Season",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1333\/106860.jpg?s=158c6cd2f0b6c08561cdb954455ed132",
              "type": "TV",
              "episodes": null,
              "start_date": null,
              "end_date": null,
              "members": 49716,
              "score": 0
            },
            {
              "mal_id": 40357,
              "rank": 20,
              "title": "Tate no Yuusha no Nariagari 3rd Season",
              "url": "https:\/\/myanimelist.net\/anime\/40357\/Tate_no_Yuusha_no_Nariagari_3rd_Season",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1406\/104631.jpg?s=f6d480878204e18ad25c5cb7d3b62f47",
              "type": "TV",
              "episodes": null,
              "start_date": null,
              "end_date": null,
              "members": 49194,
              "score": 0
            },
            {
              "mal_id": 40497,
              "rank": 21,
              "title": "Mahouka Koukou no Rettousei: Raihousha-hen",
              "url": "https:\/\/myanimelist.net\/anime\/40497\/Mahouka_Koukou_no_Rettousei__Raihousha-hen",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1788\/106668.jpg?s=7f675152d8e84a5e6b7716ac59ec20bd",
              "type": "TV",
              "episodes": null,
              "start_date": "Oct 2020",
              "end_date": null,
              "members": 47665,
              "score": 0
            },
            {
              "mal_id": 40507,
              "rank": 22,
              "title": "Arifureta Shokugyou de Sekai Saikyou 2nd Season",
              "url": "https:\/\/myanimelist.net\/anime\/40507\/Arifureta_Shokugyou_de_Sekai_Saikyou_2nd_Season",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1198\/103431.jpg?s=62a2e57f898223ee3783e61365cd5d67",
              "type": "TV",
              "episodes": null,
              "start_date": null,
              "end_date": null,
              "members": 46989,
              "score": 0
            },
            {
              "mal_id": 37932,
              "rank": 23,
              "title": "Quanzhi Gaoshou 2nd Season",
              "url": "https:\/\/myanimelist.net\/anime\/37932\/Quanzhi_Gaoshou_2nd_Season",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1296\/101468.jpg?s=e38d74e0c49930f32c7c1473a0cd5e7f",
              "type": "ONA",
              "episodes": 12,
              "start_date": "2020",
              "end_date": null,
              "members": 46764,
              "score": 0
            },
            {
              "mal_id": 41109,
              "rank": 24,
              "title": "Log Horizon: Entaku Houkai",
              "url": "https:\/\/myanimelist.net\/anime\/41109\/Log_Horizon__Entaku_Houkai",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1072\/105551.jpg?s=0ca26e5d48d23c6721a6cd7d66d6a234",
              "type": "TV",
              "episodes": null,
              "start_date": "Oct 2020",
              "end_date": null,
              "members": 45744,
              "score": 0
            },
            {
              "mal_id": 41084,
              "rank": 25,
              "title": "Made in Abyss 2",
              "url": "https:\/\/myanimelist.net\/anime\/41084\/Made_in_Abyss_2",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1778\/105456.jpg?s=dd768c4ad0ef6c36a451cd1440371399",
              "type": "Unknown",
              "episodes": null,
              "start_date": null,
              "end_date": null,
              "members": 42742,
              "score": 0
            },
            {
              "mal_id": 40935,
              "rank": 26,
              "title": "Beastars 2nd Season",
              "url": "https:\/\/myanimelist.net\/anime\/40935\/Beastars_2nd_Season",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1266\/105286.jpg?s=1535af604cf34051a292e233f9596447",
              "type": "TV",
              "episodes": null,
              "start_date": "2021",
              "end_date": null,
              "members": 41433,
              "score": 0
            },
            {
              "mal_id": 41467,
              "rank": 27,
              "title": "Bleach: Sennen Kessen-hen",
              "url": "https:\/\/myanimelist.net\/anime\/41467\/Bleach__Sennen_Kessen-hen",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1596\/106575.jpg?s=3a98462b4f174271cf4511844d0675cd",
              "type": "Unknown",
              "episodes": null,
              "start_date": null,
              "end_date": null,
              "members": 40375,
              "score": 0
            },
            {
              "mal_id": 38474,
              "rank": 28,
              "title": "Yuru Camp△ 2nd Season",
              "url": "https:\/\/myanimelist.net\/anime\/38474\/Yuru_Camp△_2nd_Season",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1557\/106678.jpg?s=72c6d45b67ff9558617d8f915b706f3b",
              "type": "TV",
              "episodes": null,
              "start_date": "Jan 2021",
              "end_date": null,
              "members": 38626,
              "score": 0
            },
            {
              "mal_id": 40174,
              "rank": 29,
              "title": "Zombieland Saga: Revenge",
              "url": "https:\/\/myanimelist.net\/anime\/40174\/Zombieland_Saga__Revenge",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1306\/102300.jpg?s=b8716b4ccc7dab3bd11ba200be652f9f",
              "type": "TV",
              "episodes": null,
              "start_date": null,
              "end_date": null,
              "members": 38595,
              "score": 0
            },
            {
              "mal_id": 35500,
              "rank": 30,
              "title": "Yuri!!! on Ice The Movie: Ice Adolescence",
              "url": "https:\/\/myanimelist.net\/anime\/35500\/Yuri_on_Ice_The_Movie__Ice_Adolescence",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1225\/93390.jpg?s=051632501eb2f445ba1eb7c54bf190bf",
              "type": "Movie",
              "episodes": 1,
              "start_date": null,
              "end_date": null,
              "members": 36040,
              "score": 0
            },
            {
              "mal_id": 41487,
              "rank": 31,
              "title": "Tensei shitara Slime Datta Ken 2nd Season Part 2",
              "url": "https:\/\/myanimelist.net\/anime\/41487\/Tensei_shitara_Slime_Datta_Ken_2nd_Season_Part_2",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1739\/106573.jpg?s=eeed9395e8eb6998d02f97e231d2dc84",
              "type": "TV",
              "episodes": null,
              "start_date": "Jul 2021",
              "end_date": "Sep 2021",
              "members": 35966,
              "score": 0
            },
            {
              "mal_id": 41623,
              "rank": 32,
              "title": "Isekai Maou to Shoukan Shoujo no Dorei Majutsu Ω",
              "url": "https:\/\/myanimelist.net\/anime\/41623\/Isekai_Maou_to_Shoukan_Shoujo_no_Dorei_Majutsu_Ω",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1705\/106918.jpg?s=6a072b1bef4a219fe5aab6325b5291e9",
              "type": "TV",
              "episodes": null,
              "start_date": "2021",
              "end_date": null,
              "members": 34894,
              "score": 0
            },
            {
              "mal_id": 37994,
              "rank": 33,
              "title": "B: The Beginning 2",
              "url": "https:\/\/myanimelist.net\/anime\/37994\/B__The_Beginning_2",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1594\/93194.jpg?s=5f3c56be3bc448698c2830aa2079570f",
              "type": "ONA",
              "episodes": 12,
              "start_date": null,
              "end_date": null,
              "members": 33765,
              "score": 0
            },
            {
              "mal_id": 41488,
              "rank": 34,
              "title": "Tensura Nikki: Tensei Shitara Slime Datta Ken",
              "url": "https:\/\/myanimelist.net\/anime\/41488\/Tensura_Nikki__Tensei_Shitara_Slime_Datta_Ken",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1992\/106609.jpg?s=bdfb7510ca099407e11038dedf168096",
              "type": "TV",
              "episodes": null,
              "start_date": "Jan 2021",
              "end_date": null,
              "members": 32045,
              "score": 0
            },
            {
              "mal_id": 33852,
              "rank": 35,
              "title": "Mekakucity Reload",
              "url": "https:\/\/myanimelist.net\/anime\/33852\/Mekakucity_Reload",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/2\/81505.jpg?s=9c375d492154ac84760e244403de134e",
              "type": "Unknown",
              "episodes": null,
              "start_date": null,
              "end_date": null,
              "members": 30898,
              "score": 0
            },
            {
              "mal_id": 40936,
              "rank": 36,
              "title": "Ore wo Suki nano wa Omae dake ka yo: Oretachi no Game Set",
              "url": "https:\/\/myanimelist.net\/anime\/40936\/Ore_wo_Suki_nano_wa_Omae_dake_ka_yo__Oretachi_no_Game_Set",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1155\/106799.jpg?s=2746231929847555dd8c9c0d6c41cc9d",
              "type": "OVA",
              "episodes": 1,
              "start_date": null,
              "end_date": null,
              "members": 30696,
              "score": 0
            },
            {
              "mal_id": 30455,
              "rank": 37,
              "title": "Kantai Collection: KanColle Zoku-hen",
              "url": "https:\/\/myanimelist.net\/anime\/30455\/Kantai_Collection__KanColle_Zoku-hen",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/5\/72868.jpg?s=903f27c7901f6e073bfaaf64ef8322d3",
              "type": "Unknown",
              "episodes": null,
              "start_date": null,
              "end_date": null,
              "members": 30643,
              "score": 0
            },
            {
              "mal_id": 40416,
              "rank": 38,
              "title": "Date A Bullet: Dead or Bullet",
              "url": "https:\/\/myanimelist.net\/anime\/40416\/Date_A_Bullet__Dead_or_Bullet",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1173\/106581.jpg?s=9b4e759bc4f5777b56ed1463fa6c3862",
              "type": "Movie",
              "episodes": 1,
              "start_date": null,
              "end_date": null,
              "members": 29674,
              "score": 0
            },
            {
              "mal_id": 40729,
              "rank": 39,
              "title": "Megalo Box 2",
              "url": "https:\/\/myanimelist.net\/anime\/40729\/Megalo_Box_2",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1942\/104386.jpg?s=2cbf5b336c7aceb79d7a907ca73bbd92",
              "type": "TV",
              "episodes": null,
              "start_date": null,
              "end_date": null,
              "members": 29587,
              "score": 0
            },
            {
              "mal_id": 41514,
              "rank": 40,
              "title": "Itai no wa Iya nano de Bougyoryoku ni Kyokufuri Shitai to Omoimasu. II",
              "url": "https:\/\/myanimelist.net\/anime\/41514\/Itai_no_wa_Iya_nano_de_Bougyoryoku_ni_Kyokufuri_Shitai_to_Omoimasu_II",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1436\/106694.jpg?s=d395802efdb5b4a093f094f0090b7a07",
              "type": "TV",
              "episodes": null,
              "start_date": null,
              "end_date": null,
              "members": 29550,
              "score": 0
            },
            {
              "mal_id": 41694,
              "rank": 41,
              "title": "Hataraku Saibou Black (TV)",
              "url": "https:\/\/myanimelist.net\/anime\/41694\/Hataraku_Saibou_Black_TV",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1598\/107059.jpg?s=47faaef91c880fd6cc32b92ca58e209e",
              "type": "TV",
              "episodes": null,
              "start_date": "Jan 2021",
              "end_date": null,
              "members": 27256,
              "score": 0
            },
            {
              "mal_id": 40421,
              "rank": 42,
              "title": "Given Movie",
              "url": "https:\/\/myanimelist.net\/anime\/40421\/Given_Movie",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1706\/106042.jpg?s=973c2843280e888e240a8b9e9287f19a",
              "type": "Movie",
              "episodes": 1,
              "start_date": null,
              "end_date": null,
              "members": 26735,
              "score": 0
            },
            {
              "mal_id": 41491,
              "rank": 43,
              "title": "Nanatsu no Taizai: Fundo no Shinpan",
              "url": "https:\/\/myanimelist.net\/anime\/41491\/Nanatsu_no_Taizai__Fundo_no_Shinpan",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/qm_50.gif?s=e1ff92a46db617cb83bfc1e205aff620",
              "type": "TV",
              "episodes": null,
              "start_date": "Oct 2020",
              "end_date": null,
              "members": 26640,
              "score": 0
            },
            {
              "mal_id": 41006,
              "rank": 44,
              "title": "Higurashi no Naku Koro ni (2020)",
              "url": "https:\/\/myanimelist.net\/anime\/41006\/Higurashi_no_Naku_Koro_ni_2020",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1808\/105271.jpg?s=7d26df7c8757e3fac2c155d5aba45f28",
              "type": "TV",
              "episodes": null,
              "start_date": null,
              "end_date": null,
              "members": 26206,
              "score": 0
            },
            {
              "mal_id": 41461,
              "rank": 45,
              "title": "Date A Live IV",
              "url": "https:\/\/myanimelist.net\/anime\/41461\/Date_A_Live_IV",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1057\/106493.jpg?s=e2fc6d7d5000b9e29b5c85ee0ce3c6ac",
              "type": "TV",
              "episodes": null,
              "start_date": null,
              "end_date": null,
              "members": 26167,
              "score": 0
            },
            {
              "mal_id": 40839,
              "rank": 46,
              "title": "Kanojo, Okarishimasu",
              "url": "https:\/\/myanimelist.net\/anime\/40839\/Kanojo_Okarishimasu",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1485\/107693.jpg?s=a79a806d78e7eb45634f82d2c927a65f",
              "type": "TV",
              "episodes": null,
              "start_date": "Jul 2020",
              "end_date": null,
              "members": 24566,
              "score": 0
            },
            {
              "mal_id": 40529,
              "rank": 47,
              "title": "No Guns Life 2nd Season",
              "url": "https:\/\/myanimelist.net\/anime\/40529\/No_Guns_Life_2nd_Season",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1477\/106613.jpg?s=5ec017f5ac36be5ce09009468ff7cdf7",
              "type": "TV",
              "episodes": 12,
              "start_date": null,
              "end_date": null,
              "members": 24166,
              "score": 0
            },
            {
              "mal_id": 41567,
              "rank": 48,
              "title": "Isekai Quartet 3",
              "url": "https:\/\/myanimelist.net\/anime\/41567\/Isekai_Quartet_3",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1083\/106801.jpg?s=6c729d34016ddaba7c62a7ef8097c4c1",
              "type": "TV",
              "episodes": null,
              "start_date": null,
              "end_date": null,
              "members": 23027,
              "score": 0
            },
            {
              "mal_id": 41168,
              "rank": 49,
              "title": "Nakitai Watashi wa Neko wo Kaburu",
              "url": "https:\/\/myanimelist.net\/anime\/41168\/Nakitai_Watashi_wa_Neko_wo_Kaburu",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1045\/106389.jpg?s=333357bb380028e6780dbeef7e953ecd",
              "type": "Movie",
              "episodes": 1,
              "start_date": "Jun 2020",
              "end_date": "Jun 2020",
              "members": 22887,
              "score": 0
            },
            {
              "mal_id": 38085,
              "rank": 50,
              "title": "Fate\/Grand Order: Shinsei Entaku Ryouiki Camelot 1 - Wandering; Agateram",
              "url": "https:\/\/myanimelist.net\/anime\/38085\/Fate_Grand_Order__Shinsei_Entaku_Ryouiki_Camelot_1_-_Wandering__Agateram",
              "image_url": "https:\/\/cdn.myanimelist.net\/images\/anime\/1535\/103385.jpg?s=053c3e710c29ce88b013fc7b12d7b067",
              "type": "Movie",
              "episodes": 1,
              "start_date": "Aug 2020",
              "end_date": "Aug 2020",
              "members": 22420,
              "score": 0
            }
          ]
        }
    """.trimIndent()
}