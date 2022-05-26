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
import com.practice.mystackexchangeusers.R
import com.practice.mystackexchangeusers.databinding.FragmentUsersBinding
import com.practice.mystackexchangeusers.presentation.adapters.UsersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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
    ): View? {

        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usersAdapter = UsersAdapter()
        setupRecyclerView()
        setupData()


        binding.btnSearch.setOnClickListener {
            findNavController().navigate(R.id.action_UsersFragment_to_UserDetailsFragment)
        }

    }

    private fun setupData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { state ->
                    when (state) {
                        is UsersViewModel.ViewState.Content -> {
                            binding.pbLoading.visibility = View.GONE
                            usersAdapter.userList = state.users.users
                        }
                        is UsersViewModel.ViewState.Error -> {
                            Toast.makeText(context, "${state.error}", Toast.LENGTH_SHORT).show()
                        }
                        UsersViewModel.ViewState.Loading -> {
                            binding.pbLoading.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvUsers.apply {
            adapter = usersAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}