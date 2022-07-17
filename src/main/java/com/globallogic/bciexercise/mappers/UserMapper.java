package com.globallogic.bciexercise.mappers;

import com.globallogic.bciexercise.domain.User;
import com.globallogic.bciexercise.dtos.SignUpUserRequest;
import com.globallogic.bciexercise.dtos.UserResponse;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toEntity(SignUpUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        if(request.getPhones() != null) {
            user.setPhones(request.getPhones().stream()
                    .map(PhoneMapper::toEntity)
                    .collect(Collectors.toList()));
        } else {
            user.setPhones(new ArrayList<>());
        }

        user.setPassword(request.getPassword());
        return user;
    }

    public static UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setCreated(user.getCreated());
        response.setLastLogin(user.getLastLogin());
        response.setToken(user.getToken());
        response.setActive(user.getActive());
        response.setPassword(user.getPassword());
        if(user.getPhones() != null) {
            response.setPhones(user.getPhones()
                    .stream()
                    .map(PhoneMapper::toResponse)
                    .collect(Collectors.toList()));
        }  else {
            response.setPhones(new ArrayList<>());
        }
        return response;
    }

}
