package com.sanmidev.yetanotheranimelist.presentation.upComingAnimes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sanmidev.yetanotheranimelist.R


/**
 * A simple [Fragment] subclass.
 */
class UpComingAnimesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_up_coming_animes, container, false)
    }

}
