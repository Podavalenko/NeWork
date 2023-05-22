package ru.netology.nework.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentNewJobBinding
import ru.netology.nework.dto.User
import ru.netology.nework.service.UserArg
import ru.netology.nework.viewmodel.JobViewModel

@AndroidEntryPoint
class NewJobFragment : Fragment() {

    private val viewModel: JobViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    companion object {
        var Bundle.showUser: User? by UserArg
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewJobBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.showUser?.let { user ->
            with(binding) {

                saveButton.setOnClickListener {
                    viewModel.editJobContent(
                        companyName = companyNameInput.text.toString(),
                        position = positionInput.text.toString(),
                        start = startInput.text.toString(),
                        end = endInput.text.toString()
                    )

                    viewModel.createNewJob(user.id)

                    findNavController().navigate(R.id.tabsFragment)
                }

            }

        }

        return binding.root
    }

}