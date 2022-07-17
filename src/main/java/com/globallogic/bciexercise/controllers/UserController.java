package com.globallogic.bciexercise.controllers;

import com.globallogic.bciexercise.domain.User;
import com.globallogic.bciexercise.dtos.SignUpUserRequest;
import com.globallogic.bciexercise.dtos.UserResponse;
import com.globallogic.bciexercise.mappers.UserMapper;
import com.globallogic.bciexercise.services.UserLoginService;
import com.globallogic.bciexercise.services.UserSignUpService;
import com.globallogic.bciexercise.services.UserSignUpServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserSignUpService userSignUpService;
    private final UserLoginService userLoginService;

    public UserController(UserSignUpService signUpUserService, UserLoginService userLoginService) {
        this.userSignUpService = signUpUserService;
        this.userLoginService = userLoginService;
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> signUp(@RequestBody SignUpUserRequest request){
        User user = UserMapper.toEntity(request);
        return new ResponseEntity<>(userSignUpService.signUp(user), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> login(HttpServletRequest request) {
        String token = getToken(request);
        UserResponse user = userLoginService.loginUser(token);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private String getToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer"))
            return header.replace("Bearer ", "");
        return null;
    }



}
