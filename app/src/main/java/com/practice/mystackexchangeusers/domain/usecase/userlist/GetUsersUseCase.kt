package com.practice.mystackexchangeusers.domain.usecase.userlist

import com.practice.mystackexchangeusers.common.Constants
import com.practice.mystackexchangeusers.common.Resource
import com.practice.mystackexchangeusers.data.remote.dto.toUsers
import com.practice.mystackexchangeusers.domain.model.Users
import com.practice.mystackexchangeusers.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {

    operator fun invoke(): Flow<Resource<Users>> = flow {
        try {
            emit(Resource.Loading<Users>())
            val users = repository.getUsers().toUsers()
            emit(Resource.Success(users))
        } catch (e: HttpException) {
            emit(Resource.Error<Users>(e.localizedMessage ?: Constants.HttpExceptionError))
        } catch (e: IOException) {
            emit(Resource.Error<Users>(Constants.IOExceptionError))
        }
    }
}