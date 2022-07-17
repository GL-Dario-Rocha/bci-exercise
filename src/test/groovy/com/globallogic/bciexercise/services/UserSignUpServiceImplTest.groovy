package com.globallogic.bciexercise.services

import com.globallogic.bciexercise.domain.User
import com.globallogic.bciexercise.errors.ApiErrorCodes
import com.globallogic.bciexercise.errors.DuplicatedUserException
import com.globallogic.bciexercise.errors.ValidationException
import com.globallogic.bciexercise.repositories.UserRepository
import com.globallogic.bciexercise.utils.Cipher
import com.globallogic.bciexercise.utils.JwtProvider
import spock.lang.Specification

class UserSignUpServiceImplTest extends Specification {
    UserRepository userRepository
    UserSignUpServiceImpl userService
    JwtProvider jwtProvider
    Cipher cypher

    def setup() {
        userRepository = Mock(UserRepository)
        jwtProvider = Mock(JwtProvider)
        cypher = Mock(Cipher)
        userService = new UserSignUpServiceImpl(userRepository, jwtProvider, cypher)
    }

    def "should persist user if have a valid mail and password(other fields are optionals)" () {
        given:
            def userEmail = "valid@email.com"
            def password = "a2asfGfdfdf4"

            userRepository.findByEmail(userEmail) >> Optional.empty()
            1 * jwtProvider.generateToken(userEmail) >> "randomToken"

            def user = new User()
            userRepository.save(user) >> user
            user.setEmail(userEmail)
            user.setPassword(password)
        when:
            def userResponse = userService.signUp(user)
        then:
            userResponse.getEmail() == user.getEmail()
            userResponse.getToken() == "randomToken"
            userResponse.getActive()
            userResponse.getCreated() != null
            userResponse.getLastLogin() != null
            userResponse.getCreated() == userResponse.getLastLogin()
            userResponse.getPhones().isEmpty()
            1 * cypher.encrypt(password)
            1 * userRepository.save(user)
    }

    def "should throw exception if the request does not have the email" () {
        given:
            def user = new User()
            user.setPassword("a2asfGfdfdf4")
            userRepository.save(user) >> user
        when:
            userService.signUp(user)
        then:
            def exception = thrown(ValidationException)
            exception.getErrors()
                    .stream()
                    .anyMatch(error ->
                            error.getCode() == ApiErrorCodes.INVALID_EMAIL.getCode()
                            && error.getDetail() == "email can not be empty"
                            && error.getTimestamp() != null)
            exception.getErrors().size() == 1
    }

    def "should throw exception if the request does not have the password" () {
        given:
            def user = new User()
            user.setEmail("valid@email.com")
            userRepository.save(user) >> user
        when:
            userService.signUp(user)
        then:
            def exception = thrown(ValidationException)
            exception.getErrors()
                    .stream()
                    .anyMatch(error
                            -> error.getCode() == ApiErrorCodes.INVALID_PASSWORD.getCode()
                    && error.getDetail() == "password can not be empty"
                    && error.getTimestamp() != null)
            exception.getErrors().size() == 1
    }

    def "should throw exception if the user already exist" () {
        given:
            def userEmail = "valid@email.com"
            def user = new User()
            user.setEmail(userEmail)
            user.setPassword("a2asfGfdfdf4")
            userRepository.findByEmail(userEmail) >> Optional.of(user)
        when:
            userService.signUp(user)
        then:
            def exception = thrown(DuplicatedUserException)
            exception.getError().getCode() == ApiErrorCodes.ALREADY_EXISTING_RESOURCE.getCode()
            exception.getError().getDetail() == "user already exist"
            exception.getError().getTimestamp() != null
    }


}
