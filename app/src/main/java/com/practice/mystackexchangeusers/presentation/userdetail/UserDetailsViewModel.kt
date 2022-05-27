package com.practice.mystackexchangeusers.presentation.userdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.mystackexchangeusers.common.Constants
import com.practice.mystackexchangeusers.common.Resource
import com.practice.mystackexchangeusers.domain.model.User
import com.practice.mystackexchangeusers.domain.usecase.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val useCases: UserUseCases,
    private val state: SavedStateHandle
) : ViewModel() {

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState: Flow<ViewState> = _viewState

    init {
        loadContent()
    }

    private fun loadContent() {
        val userId = state.get<Int>(Constants.NAV_ARGS_USER_ID_NAME)
        useCases.getUserUseCase(userId.toString()).onEach { result ->
            _viewState.value = when(result) {
                is Resource.Loading -> ViewState.Loading
                is Resource.Error ->  ViewState.Error(
                    result.message ?: "An unknown error has occurred"
                )
                is Resource.Success -> result.data?.let {
                    ViewState.Content(it)
                } ?: ViewState.Error("An error has occurred fetching user")
            }
        }.launchIn(viewModelScope)
    }

    sealed class ViewState() {
        object Loading : ViewState()
        data class Error(val error: String) : ViewState()
        data class Content(val user: User) : ViewState()
    }
}