package com.practice.mystackexchangeusers.presentation.userlist

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practice.mystackexchangeusers.common.onQueryTextChanged
import com.practice.mystackexchangeusers.databinding.FragmentUsersBinding
import com.practice.mystackexchangeusers.presentation.adapters.UsersAdapter
import com.practice.mystackexchangeusers.presentation.userlist.UsersViewModel.Action
import com.practice.mystackexchangeusers.presentation.userlist.UsersViewModel.ViewEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class UsersFragment : Fragment() {

    private val viewModel: UsersViewModel by viewModels()
    private var _binding: FragmentUsersBinding? = null
    private lateinit var usersAdapter: UsersAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupData()
        setUpEventFlow()
        setupSearch()
    }

    private fun setupRecyclerView() {
        binding.rvUsers.apply {
            usersAdapter = UsersAdapter()
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }

        usersAdapter.onUserClickedListener = { user ->
            viewModel.send(Action.UserClicked(user))
        }
    }

    private fun setupData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { state ->
                    when (state) {
                        is UsersViewModel.ViewState.Content -> {
                            binding.pbLoading.visibility = View.GONE
                            usersAdapter.updateUserList(state.users)
                        }
                        is UsersViewModel.ViewState.Error -> {
                            binding.pbLoading.visibility = View.GONE
                            Toast.makeText(
                                context,
                                "Error occurred: ${state.error}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        UsersViewModel.ViewState.Loading -> {
                            binding.pbLoading.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun setUpEventFlow() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewEvent.collectLatest { event ->
                    when (event) {
                        is ViewEvent.GoToUserDetails -> {
                            val action =
                                UsersFragmentDirections.actionUsersFragmentToUserDetailsFragment(
                                    event.user.userId, event.user.userName
                                )
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }

    private fun setupSearch() {
        binding.svSearch.onQueryTextChanged {
            viewModel.send(Action.SearchUpdated(it))
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filteredResult.collectLatest { filteredUserList ->
                    usersAdapter.updateUserList(filteredUserList)

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}