package com.globallogic.bciexercise.controllers

import com.globallogic.bciexercise.domain.User
import com.globallogic.bciexercise.dtos.UserResponse
import com.globallogic.bciexercise.services.UserLoginService
import com.globallogic.bciexercise.services.UserSignUpService
import org.spockframework.spring.SpringBean
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

@AutoConfigureMockMvc
@WebMvcTest
class UserControllerTest extends Specification {


    private UserSignUpService signUpService = Mock(UserSignUpService)
    private UserLoginService loginService = Mock(UserLoginService)
    @SpringBean
    private UserController userController = new UserController(signUpService, loginService)

    private MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build()

    def "when context is loaded then all expected beans are created"() {
        expect: "the UserController is created"
        userController
    }

    def "when POST users/sign-up is performed then the response has status CREATED and content is User"() {
        given:
            def userResponse = new UserResponse()
            signUpService.signUp(_ as User) >> userResponse
        when:
            def result = mockMvc.perform(MockMvcRequestBuilders
                    .post("/users/sign-up")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                            """
                    {
                        "name": "Juan Perez",
                        "email": "juan.perez@gmail.cam",
                        "password": "a2asfGfdfdf4",
                        "phones": [
                            {
                            "number": 32165487,
                            "citycode": 54,
                            "countrycode": "ARG"
                            }
                        ]
                    }
"""
                    )).andReturn()
        then:
            result.response.status == HttpStatus.CREATED.value()
            result.response.contentType == MediaType.APPLICATION_JSON_VALUE
    }

    def "when GET users/login is performed then the response has status OK and content is User"() {
        given:
            def userResponse = new UserResponse()
            loginService.loginUser(_ as String) >> userResponse
        when:
            def result = mockMvc.perform(MockMvcRequestBuilders
                    .get("/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiO...")
            ).andReturn()
        then:
            result.response.status == HttpStatus.OK.value()
            result.response.contentType == MediaType.APPLICATION_JSON_VALUE
    }

}
