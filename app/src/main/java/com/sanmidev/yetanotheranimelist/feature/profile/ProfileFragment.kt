package com.sanmidev.yetanotheranimelist.feature.profile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sanmidev.yetanotheranimelist.R
import com.sanmidev.yetanotheranimelist.databinding.ProfileFragmentBinding


class ProfileFragment : Fragment() {

    private var profileFragmentBinding : ProfileFragmentBinding? = null

    val binding : ProfileFragmentBinding
    get() = profileFragmentBinding!!

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       profileFragmentBinding = ProfileFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolBar()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }


    private fun initToolBar() {
        binding.include3.toolbar.title = getString(R.string.profile_txt)
    }

}
