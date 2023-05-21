package ru.netology.nework.fragment

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.netology.nework.R
import ru.netology.nework.adapter.OnInteractionListener
import ru.netology.nework.adapter.PostAdapter
import ru.netology.nework.databinding.FragmentListPostBinding
import ru.netology.nework.dto.Post
import ru.netology.nework.viewmodel.AuthViewModel
import ru.netology.nework.viewmodel.PostViewModel
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.nework.adapter.PostRecyclerView
import ru.netology.nework.fragment.CardPostFragment.Companion.showPost
import ru.netology.nework.fragment.NewPostFragment.Companion.textArg
import ru.netology.nework.service.MediaLifecycleObserver
import androidx.browser.customtabs.CustomTabsIntent
import dagger.hilt.android.AndroidEntryPoint

private const val BASE_URL = "https://netomedia.ru/api/media"

@AndroidEntryPoint
class ListPostFragment : Fragment() {

    private var recyclerView: PostRecyclerView? = null

    private val postViewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val authViewModel: AuthViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentListPostBinding.inflate(
            inflater,
            container,
            false
        )

        val mediaObserver = MediaLifecycleObserver()

        lifecycle.addObserver(mediaObserver)

        val postAdapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                if (authViewModel.authenticated) {
                    postViewModel.likeById(post.id)
                } else {
                    findNavController().navigate(R.id.authFragment)
                }
            }

            override fun onDisLike(post: Post) {
                if (authViewModel.authenticated) {
                    postViewModel.disLikeById(post.id)
                } else {
                    findNavController().navigate(R.id.authFragment)
                }
            }

            override fun onSinglePost(post: Post) {
                findNavController().navigate(R.id.cardPostFragment,
                    Bundle().apply {
                        showPost = post
                    })
            }

            override fun onEdit(post: Post) {
                postViewModel.edit(post)
                binding.addNewPost.visibility = View.GONE
                findNavController().navigate(
                    R.id.editPostFragment,
                    Bundle().apply {
                        textArg = post.content
                    })
            }

            override fun onRemove(post: Post) {
                postViewModel.removeById(post.id)
            }

            override fun onFullScreenImage(post: Post) {
                findNavController().navigate(R.id.imageFragment, Bundle().apply { textArg = post.attachment?.url })
            }

            override fun onPlayAudio(post: Post) {
                mediaObserver.apply {
                    player?.setDataSource(
                        BASE_URL
                    )
                }.play()
            }

            override fun onLink(url: String) {
                CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .build()
                    .launchUrl(requireContext(), Uri.parse(url))
            }

        })

        binding.postsContainer.adapter = postAdapter

        postViewModel.data.observe(viewLifecycleOwner, { state ->
            postAdapter.submitList(state.posts)
        })

        postViewModel.dataState.observe(viewLifecycleOwner, { state ->
            with(binding) {
                progress.isVisible = state.loading
                swiperefresh.isRefreshing = state.refreshing
                if (state.error) {
                    error.visibility = View.VISIBLE
                    tryAgainButton.setOnClickListener {
                        postViewModel.tryAgain()
                        error.visibility = View.GONE
                    }
                }
            }
        })

        with(binding) {
            swiperefresh.setOnRefreshListener {
                postViewModel.loadPosts()
            }

            addNewPost.setOnClickListener {
                if (authViewModel.authenticated) {
                    findNavController().navigate(R.id.newPostFragment)
                } else {
                    findNavController().navigate(R.id.authFragment)
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView = null
    }

    override fun onResume() {
        recyclerView?.createPlayer()
        super.onResume()
    }

    override fun onPause() {
        recyclerView?.releasePlayer()
        super.onPause()
    }

    override fun onStop() {
        recyclerView?.releasePlayer()
        super.onStop()
    }
}