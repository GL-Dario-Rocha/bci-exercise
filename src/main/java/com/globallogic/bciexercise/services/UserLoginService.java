package com.globallogic.bciexercise.services;

import com.globallogic.bciexercise.dtos.UserResponse;

public interface UserLoginService {

    UserResponse loginUser(String token);
}
