package ru.netology.nework.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.nework.R
import ru.netology.nework.adapter.EventAdapter
import ru.netology.nework.adapter.OnEventListener
import ru.netology.nework.databinding.FragmentListEventsBinding
import ru.netology.nework.dto.Event
import ru.netology.nework.fragment.CardEventFragment.Companion.showEvent
import ru.netology.nework.fragment.NewPostFragment.Companion.textArg
import ru.netology.nework.viewmodel.AuthViewModel
import ru.netology.nework.viewmodel.EventViewModel

@AndroidEntryPoint
class ListEventsFragment : Fragment() {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentListEventsBinding.inflate(
            inflater,
            container,
            false
        )

        val eventViewModel: EventViewModel by viewModels(
            ownerProducer = ::requireParentFragment
        )

        val authViewModel: AuthViewModel by viewModels(
            ownerProducer = ::requireParentFragment
        )

        val eventAdapter = EventAdapter(object : OnEventListener {
            override fun onEdit(event: Event) {
                eventViewModel.editEvent(event)
                binding.addNewEvent.visibility = View.GONE
                findNavController().navigate(
                    R.id.editEventFragment,
                    Bundle().apply {
                        textArg = event.content
                    })
            }

            override fun onLike(event: Event) {
                if (authViewModel.authenticated) {
                    eventViewModel.likeById(event.id)
                } else {
                    findNavController().navigate(R.id.authFragment)
                }
            }

            override fun onDisLike(event: Event) {
                if (authViewModel.authenticated) {
                    eventViewModel.disLikeById(event.id)
                } else {
                    findNavController().navigate(R.id.authFragment)
                }
            }

            override fun onTakePart(event: Event) {
                eventViewModel.takePartEvent(event.id)
            }

            override fun onUnTakePart(event: Event) {
                eventViewModel.unTakePartEvent(event.id)
            }

            override fun onSingleEvent(event: Event) {
                findNavController().navigate(R.id.cardEventFragment,
                    Bundle().apply
                    {
                        showEvent = event
                    })
            }

            override fun onRemove(event: Event) {
                eventViewModel.removeEventById(event.id)
            }

            override fun onFullImage(event: Event) {
                findNavController().navigate(R.id.imageFragment)
            }

            override fun onLink(url: String) {
                CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .build()
                    .launchUrl(requireContext(), Uri.parse(url))
            }

        })

        binding.eventsContainer.adapter = eventAdapter

        eventViewModel.data.observe(viewLifecycleOwner, { state ->
            eventAdapter.submitList(state.events)
        })

        eventViewModel.dataState.observe(viewLifecycleOwner, { state ->
            with(binding) {
                progress.isVisible = state.loading
                swiperefresh.isRefreshing = state.refreshing
                if (state.error) {
                    error.visibility = View.VISIBLE
                    tryAgainButton.setOnClickListener {
                        eventViewModel.tryAgain()
                        error.visibility = View.GONE
                    }
                }
            }
        })

        with(binding) {
            addNewEvent.setOnClickListener {
                if (authViewModel.authenticated) {
                    findNavController().navigate(R.id.newEventFragment)
                } else {
                    findNavController().navigate(R.id.authFragment)
                }
            }
            swiperefresh.setOnRefreshListener {
                eventViewModel.getAllEvents()
            }
        }

        return binding.root
    }

}