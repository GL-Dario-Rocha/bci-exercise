package com.globallogic.bciexercise.services;

import com.globallogic.bciexercise.domain.User;
import com.globallogic.bciexercise.dtos.UserResponse;

public interface UserSignUpService {
    UserResponse signUp(User user);

}
