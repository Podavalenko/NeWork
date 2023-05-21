package ru.netology.nework.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.databinding.FragmentImageBinding
import ru.netology.nework.fragment.NewPostFragment.Companion.textArg
import ru.netology.nework.service.imageLoad

@AndroidEntryPoint
class ImageFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentImageBinding.inflate(
            inflater,
            container,
            false
        )

        with(binding) {
            arguments?.textArg.let {
                if (it != null) {
                    showImage.imageLoad(it)
                }
            }

        }

        return binding.root
    }

}