package com.practice.mystackexchangeusers.presentation.userlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import com.practice.mystackexchangeusers.presentation.userlist.UsersViewModel.ViewState
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock


class UsersViewModelTest {

    @get: Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    val getUsersUseCaseSuccessMock = mock<GetUsersUseCase> {
        on { invoke() } doReturn flow { Resource.Success(fullList) }
    }

    private val usecasesSuccess =
        UserUseCases(getUsersUseCaseSuccessMock, mock(GetUserUseCase::class.java))

    val getUsersUseCaseErrorMock = mock<GetUsersUseCase> {
        on { invoke() } doReturn flow { Resource.Error<String>(ERROR_MESSAGE) }
    }

    private val usecasesError =
        UserUseCases(getUsersUseCaseErrorMock, mock(GetUserUseCase::class.java))

    private val testDispatchers = TestDispatchers()
    private lateinit var viewmodel: UsersViewModel


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
        viewmodel = UsersViewModel(
            useCases = usecasesSuccess,
            dispatchers = testDispatchers
        )
        job.join()
        job.cancel()
    }

    @Test
    fun `view state contains list`() = runTest {
        viewmodel = UsersViewModel(
            useCases = usecasesSuccess,
            dispatchers = testDispatchers
        )
        val job = launch {
            viewmodel.viewState.test {
                assertThat(awaitItem()).isEqualTo(ViewState.Loading)
                assertThat(awaitItem()).isEqualTo(ViewState.Content(fullList))
            }
        }
        job.join()
        job.cancel()
    }

    @Test
    fun `view state contains error`() = runTest {
        val job = launch {
            viewmodel.viewState.test {
                assertThat(awaitItem()).isEqualTo(ViewState.Loading)
                assertThat(awaitItem()).isEqualTo(ViewState.Error(ERROR_MESSAGE))
            }
        }
        viewmodel = UsersViewModel(
            useCases = usecasesError,
            dispatchers = testDispatchers
        )
        job.join()
        job.cancel()
    }

    @Test
    fun `Empty filtered view state is false`() = runTest {
        val job = launch {
            viewmodel.viewState.test {
                assertThat(awaitItem()).isEqualTo(ViewState.Filtered(false))
                cancelAndConsumeRemainingEvents()
            }
        }
        viewmodel = UsersViewModel(
            useCases = usecasesSuccess,
            dispatchers = testDispatchers
        )
        viewmodel.textChanged("")
        job.join()
        job.cancel()
    }

    @Test
    fun `filtered view state is true`() = runTest {
        val job = launch {
            viewmodel.viewState.test {
                assertThat(awaitItem()).isEqualTo(ViewState.Filtered(true))
                cancelAndConsumeRemainingEvents()
            }
        }
        viewmodel = UsersViewModel(
            useCases = usecasesSuccess,
            dispatchers = testDispatchers
        )
        viewmodel.textChanged("username2")
        job.join()
        job.cancel()
    }

}

private val fullList
    get() = listOf(
        User(userId = 1234, userName = "username"),
        User(userId = 2345, userName = "username2")
    )

const val ERROR_MESSAGE = "An Error has occurred"