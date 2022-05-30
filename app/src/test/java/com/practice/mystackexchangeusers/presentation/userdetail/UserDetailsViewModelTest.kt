package com.practice.mystackexchangeusers.presentation.userdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.Event
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.practice.mystackexchangeusers.MainCoroutineRule
import com.practice.mystackexchangeusers.TestDispatchers
import com.practice.mystackexchangeusers.common.Resource
import com.practice.mystackexchangeusers.domain.model.User
import com.practice.mystackexchangeusers.domain.usecase.UserUseCases
import com.practice.mystackexchangeusers.domain.usecase.userdetail.GetUserUseCase
import com.practice.mystackexchangeusers.domain.usecase.userlist.GetUsersUseCase
import com.practice.mystackexchangeusers.presentation.userdetail.UserDetailsViewModel.ViewState
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class UserDetailsViewModelTest {


    @get: Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val getUserUseCaseSuccessMock = mock<GetUserUseCase> {
        on { invoke(any()) } doReturn flow { Resource.Success(user) }
    }

    private val usecasesSuccess = UserUseCases(
        mock(
            GetUsersUseCase::class.java
        ),
        getUserUseCaseSuccessMock
    )

    private val getUserUseCaseErrorMock = mock<GetUserUseCase> {
        on { invoke(any()) } doReturn flow { Resource.Error<String>(ERROR_MESSAGE) }
    }

    private val usecasesError = UserUseCases(
        mock(
            GetUsersUseCase::class.java
        ),
        getUserUseCaseErrorMock
    )

    private val savedStateHandle: SavedStateHandle = mock(SavedStateHandle::class.java)

    private val testDispatchers = TestDispatchers()
    private lateinit var viewmodel: UserDetailsViewModel

    @Test
    fun `should emit default value`() = runTest {
        val job = launch {
            viewmodel.viewState.test {
                val actual = cancelAndConsumeRemainingEvents()
                assertThat(actual).containsExactly(
                    Event.Item(
                        ViewState.Loading
                    )
                )
            }
        }
        viewmodel = UserDetailsViewModel(
            useCases = usecasesSuccess,
            state = savedStateHandle
        )
        job.join()
        job.cancel()
    }

    @Test
    fun `view state contains user`() = runTest {
        val job = launch {
            viewmodel.viewState.test {
                assertThat(awaitItem()).isEqualTo(ViewState.Loading)
                assertThat(awaitItem()).isEqualTo(ViewState.Content(user))
            }
        }
        viewmodel = UserDetailsViewModel(
            useCases = usecasesSuccess,
            state = savedStateHandle
        )
        job.join()
        job.cancel()
    }

    @Test
    fun `view state contains error`() = runTest {
        val job = launch {
            viewmodel.viewState.test {
                assertThat(awaitItem()).isEqualTo(ViewState.Loading)
                assertThat(awaitItem())
                    .isEqualTo(ViewState.Error(ERROR_MESSAGE))
            }
        }
        viewmodel = UserDetailsViewModel(
            useCases = usecasesError,
            state = savedStateHandle
        )
        job.join()
        job.cancel()
    }


}

private val user
    get() =
        User(userId = 1234, userName = "username", reputation = 10)

const val ERROR_MESSAGE = "An Error has occurred"