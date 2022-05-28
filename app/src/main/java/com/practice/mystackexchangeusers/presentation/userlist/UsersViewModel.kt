package com.practice.mystackexchangeusers.presentation.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.mystackexchangeusers.common.Resource
import com.practice.mystackexchangeusers.domain.model.User
import com.practice.mystackexchangeusers.domain.usecase.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val useCases: UserUseCases
) : ViewModel() {
    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState: Flow<ViewState> = _viewState

    private var rawList = MutableStateFlow(emptyList<User>())

    private val _viewEvent = MutableSharedFlow<ViewEvent>()
    val viewEvent: Flow<ViewEvent> = _viewEvent

    private val queryStateFlow = MutableStateFlow("")

    init {
        loadContent()
    }

    fun textChanged(text: String) {
        if (text.isNotEmpty()) {
            queryStateFlow.value = text
            _viewState.value = ViewState.Filtered(true)
        } else {
            queryStateFlow.value = ""
            _viewState.value = ViewState.Filtered(false)
            _viewState.value = ViewState.Content(rawList.value.prepareForView(USER_FETCH_SIZE))
        }
    }

    private val _filteredResult = queryStateFlow.combine(rawList) { query, users ->
        users.filter { user -> user.userName.startsWith(query, ignoreCase = true) }
            .prepareForView(USER_FETCH_SIZE)
    }
    val filteredResult = _filteredResult

    fun send(action: Action) = viewModelScope.launch {
        when (action) {
            is Action.UserClicked -> _viewEvent.emit(ViewEvent.GoToUserDetails(action.user))
            is Action.SearchUpdated -> textChanged(action.string)
        }
    }

    private fun loadContent() =
        useCases.getUsersUseCase().onEach { result ->
            _viewState.value = when (result) {
                is Resource.Loading -> ViewState.Loading
                is Resource.Error -> ViewState.Error(
                    result.message ?: "An unknown error has occurred"
                )
                is Resource.Success -> {
                    rawList.value = result.data ?: emptyList()
                    ViewState.Content(
                        result.data?.prepareForView(USER_FETCH_SIZE) ?: emptyList()
                    )
                }
            }
        }.launchIn(viewModelScope)

    private fun List<User>.prepareForView(userFetchSize: Int) = sortedBy { it.userName }.take(userFetchSize)

    sealed class ViewEvent() {
        data class GoToUserDetails(val user: User) : ViewEvent()
    }

    sealed class ViewState() {
        object Loading : ViewState()
        data class Error(val error: String) : ViewState()
        data class Content(val users: List<User>) : ViewState()
        data class Filtered(val isFiltered: Boolean) : ViewState()
    }

    sealed class Action() {
        data class UserClicked(val user: User) : Action()
        data class SearchUpdated(val string: String) : Action()
    }

    companion object {
        const val USER_FETCH_SIZE = 20
    }
}