package com.practice.mystackexchangeusers.domain.usecase.userdetail

import com.practice.mystackexchangeusers.common.Constants
import com.practice.mystackexchangeusers.common.Resource
import com.practice.mystackexchangeusers.data.remote.dto.toUser
import com.practice.mystackexchangeusers.domain.model.User
import com.practice.mystackexchangeusers.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository
) {

    operator fun invoke(userId: String): Flow<Resource<User>> = flow {
        try {
            emit(Resource.Loading<User>())
            val user = repository.getUser(userId).toUser()
            emit(Resource.Success(user))
        } catch (e: HttpException) {
            emit(Resource.Error<User>(e.localizedMessage ?: Constants.HttpExceptionError))
        } catch (e: IOException) {
            emit(Resource.Error<User>(Constants.IOExceptionError))
        }
    }
}