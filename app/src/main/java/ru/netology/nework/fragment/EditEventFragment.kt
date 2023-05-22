package ru.netology.nework.fragment

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentEditEventBinding
import ru.netology.nework.enumeration.AttachmentType
import ru.netology.nework.service.AndroidUtils
import ru.netology.nework.service.StringArg
import ru.netology.nework.viewmodel.EventViewModel

@AndroidEntryPoint
class EditEventFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: EventViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentEditEventBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.textArg.let(binding.editContent::setText)

        val pickPhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    ImagePicker.RESULT_ERROR -> {
                        Snackbar.make(
                            binding.root,
                            ImagePicker.getError(it.data),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Activity.RESULT_OK -> {
                        val uri: Uri? = it.data?.data
                        viewModel.changeAttachment(uri, uri?.toFile(), AttachmentType.IMAGE)
                    }
                }
            }

        binding.pickPhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.GALLERY)
                .galleryMimeTypes(
                    arrayOf(
                        "image/png",
                        "image/jpeg",
                    )
                )
                .createIntent(pickPhotoLauncher::launch)
        }

        binding.takePhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.CAMERA)
                .createIntent(pickPhotoLauncher::launch)
        }

        viewModel.attachment.observe(viewLifecycleOwner) { model ->
            if (model.uri == null) {
                binding.photoContainer.visibility = View.GONE

            }
            when (model.type) {
                AttachmentType.IMAGE -> {
                    binding.photoContainer.visibility = View.VISIBLE
                    binding.photo.setImageURI(model.uri)
                }
                else -> binding.photoContainer.visibility = View.GONE
            }
        }


        with(binding) {
            group.visibility = View.VISIBLE

            saveButton.setOnClickListener {
                viewModel.editEventContent(editContent.text.toString())
                viewModel.createNewEventOnLine()
                AndroidUtils.hideKeyboard(requireView())
                binding.group.visibility = View.GONE
                findNavController().navigate(R.id.tabsFragment)
            }

            cancelEditButton.setOnClickListener {
                with(binding.editContent) {
                    AndroidUtils.hideKeyboard(this)
                    binding.group.visibility = View.GONE
                    findNavController().navigate(R.id.tabsFragment)
                }
            }

        }

        return binding.root
    }

}