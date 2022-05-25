package com.practice.mystackexchangeusers.domain.usecase

import com.practice.mystackexchangeusers.domain.usecase.userdetail.GetUserUseCase
import com.practice.mystackexchangeusers.domain.usecase.userlist.GetUsersUseCase

data class UserUseCases(val getUsersUseCase: GetUsersUseCase, val getUserUseCase: GetUserUseCase)