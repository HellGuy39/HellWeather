package com.hellguy39.hellweather.domain.usecase.local

data class UserLocationUseCases(
    val addUserLocationUseCase: AddUserLocationUseCase,
    val deleteUserLocationUseCase: DeleteUserLocationUseCase,
    val getUserLocationListUseCase: GetUserLocationListUseCase
)
