package com.practice.mystackexchangeusers.presentation.userdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.practice.mystackexchangeusers.common.loadImage
import com.practice.mystackexchangeusers.databinding.FragmentUserDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailsFragment : Fragment() {

    private var _binding: FragmentUserDetailsBinding? = null
    private val viewModel: UserDetailsViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { state ->
                    with(binding) {
                        when (state) {
                            is UserDetailsViewModel.ViewState.Content -> {
                                val user = state.user
                                with(user) {
                                    ivProfile.loadImage(requireContext(), user.profileImage ?: "")
                                    tvUserName.text = userName
                                    tvReputation.text = reputation.toString()
                                    tvBadgesGold.text = badges?.gold.toString()
                                    tvBadgesSilver.text = badges?.silver.toString()
                                    tvBadgesBronze.text = badges?.bronze.toString()
                                    tvLocation.text = location
                                    tvCreationDate.text = creationDate.toString()
                                    pbLoading.visibility = View.GONE
                                }
                            }
                            is UserDetailsViewModel.ViewState.Error -> {
                                pbLoading.visibility = View.GONE
                                Toast.makeText(
                                    context,
                                    "Error occurred: ${state.error}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            UserDetailsViewModel.ViewState.Loading -> {
                                pbLoading.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}